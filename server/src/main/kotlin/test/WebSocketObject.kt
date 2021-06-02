package test

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
    // val messageFunctions:MutableMap<String, (user:Session,data:Message) -> Unit> = mutableMapOf()
    val messageFunctions:MutableMap<String, (user:Session,data:String) -> Unit> = mutableMapOf()

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
            println("OnConnect")
            println(user)

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
        fun onMessage(user: Session, message: String){
            println(message);

             val parsed_message = Gson().fromJson(message, Message::class.java)

            // val parsed_message = Gson().fromJson(message, Message::class.java) as Message<MutableMap<String,String>>
            // println(parsed_message)
             var xD:MutableMap<String, String> = parsed_message.info as MutableMap<String, String>


            for ((key, value) in messageFunctions){
                // value(user, parsed_message);
                value(user,message)
            }

            println("onMessage")
        }
    }
}