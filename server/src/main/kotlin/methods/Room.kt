package methods

import org.eclipse.jetty.websocket.api.Session

class Room {
    val table = listOf<Session>()

    fun small_broadcast(){
        for (x in table){

        }
    }
}