package methods.WebSocket

import com.google.gson.Gson
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket
import java.util.concurrent.ConcurrentLinkedQueue

import java.util.Queue
import java.io.IOException





object WebSocketObject{
    val sessions: Queue<Session> = ConcurrentLinkedQueue()
    val messageFunctions:MutableMap<String, (data:Message) -> Unit> = mutableMapOf()

    /**
     *  funckja, która odpowiada za wysyłanie wiadomości
     *  do wszystkich dostępnych klientów
     */
    fun broadcast(message:String){
        for(x in sessions){
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
            println(user)
        }


        /**
         * działania przy odłączeniu klienta
         */
        @OnWebSocketClose
        fun onClose(user: Session, statusCode: Int, reason: String?) {
            sessions.remove(user);
        }


        /**
         *  działania przy otrzymaniu komunikatu od klienta
         */
        @OnWebSocketMessage
        @Throws(IOException::class)
        fun onMessage(webSocket: Session, message: String){
            val parsed_message= Gson().fromJson(message, Message::class.java)

            for ((key, value) in messageFunctions){
                value(parsed_message);
            }
        }
    }
}