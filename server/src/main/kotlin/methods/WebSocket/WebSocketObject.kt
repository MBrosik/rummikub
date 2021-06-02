package main.kotlin.methods.WebSocket

import com.google.gson.Gson
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket
import java.io.IOException
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.collections.ArrayList


object WebSocketObject {
    val sessions: Queue<Session> = ConcurrentLinkedQueue()
    val messageFunctions = ArrayList<(user:Session,data:MessageData) -> Unit>()

    /**
     *  funckja, która odpowiada za wysyłanie wiadomości
     *  do wszystkich dostępnych klientów
     */
    fun broadcast(message: String) {
        for (x in sessions) {
            x.remote.sendString(message)
        }
    }

    @WebSocket
    class WebSocketServer {

        /**
         * Działanie przy podłączeniu się klienta
         */
        @OnWebSocketConnect
        fun onConnect(user: Session) {
            sessions.add(user);
            println("OnConnect")
            println(user)

            println("localAddress: ${user.localAddress}");
            println("user.policy: ${user.policy}")
            println("user.remote: ${user.remote}")
            println("user.remoteAddress: ${user.remoteAddress}")
            println("user.protocolVersion: ${user.protocolVersion}")
            println("user.hashCode: ${user.hashCode()}")


            user.remote.sendString("Siemka")
        }


        /**
         * działania przy odłączeniu klienta
         */
        @OnWebSocketClose
        fun onClose(user: Session, statusCode: Int, reason: String?) {
            sessions.remove(user);
            println("onClose")
        }


        /**
         *  działania przy otrzymaniu komunikatu od klienta
         */
        @OnWebSocketMessage
        @Throws(IOException::class)
        fun onMessage(user: Session, message: String) {
            val parsedMessage = Gson().fromJson(message, MessageData::class.java)

            //val exampleMap = parsedMessage.info as MutableMap<String, String>
            //var aa = exampleMap["aaa"]!!

//            user.remote.


            for ( value in messageFunctions) {
                // value(user, parsed_message);
                value(user, parsedMessage)
            }

            println("onMessage")

            println("localAddress: ${user.localAddress}");
            println("user.policy: ${user.policy}")
            println("user.remote: ${user.remote}")
            println("user.remoteAddress: ${user.remoteAddress}")
            println("user.protocolVersion: ${user.protocolVersion}")
            println("user.hashCode: ${user.hashCode()}")
        }
    }
}