package main.kotlin.methods.WebSocket

import com.google.gson.Gson
import main.kotlin.methods.roomManage.RoomObject
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket
import java.io.IOException


object WebSocketObject {
    /**
     * objekt sesji, jako key przechowujemy hashCode (identyfikator) sesji,
     * a jako value dane np o połączeniu i o pokojach
     */
    val sessions: MutableMap<Int, SessionStructure> = mutableMapOf()


    /**
     *  funckja, która odpowiada za wysyłanie wiadomości
     *  do wszystkich dostępnych klientów
     */
    fun broadcast(message: String) {
        for ((key, value) in sessions) {
            value.session.remote.sendString(message)
        }
    }


    @WebSocket
    class WebSocketServer {

        /**
         * Działanie przy podłączeniu się klienta
         */
        @OnWebSocketConnect
        fun onConnect(user: Session) {
            sessions[user.hashCode()] = SessionStructure(user)


            val sendMap = mapOf<String, String>(
                "type" to "onConnect",
                "data" to "Witaj Websockecie!!"
            )
            user.remote.sendString(Gson().toJson(sendMap))
        }


        /**
         * działania przy odłączeniu klienta
         */
        @OnWebSocketClose
        fun onClose(user: Session, statusCode: Int, reason: String?) {
            /**
             * Usuwanie z Gracza z pokoju
             */
            RoomObject.removeFromRoom(sessions[user.hashCode()]!!)
            sessions.remove(user.hashCode())
        }


        /**
         *  działania przy otrzymaniu komunikatu od klienta
         */
        @OnWebSocketMessage
        @Throws(IOException::class)
        fun onMessage(user: Session, message: String) {
            /**
             * filtrowanie wiadomości
             */
            val parsedMessage = Gson().fromJson(message, MessageData::class.java)


            val userData = sessions[user.hashCode()]!!

            when (parsedMessage.type) {
                "joinRoom" -> RoomObject.searchForRoom(sessions[user.hashCode()]!!, parsedMessage.data)
                "playerTurn" -> {
                    if (userData.roomClass != null) {
                        userData.roomClass!!.playerFinishedTurn(userData, parsedMessage.data)
                    }
                }
            }
        }
    }
}