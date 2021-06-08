package main.kotlin.methods.roomManage

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import main.kotlin.methods.WebSocket.MessageData
import main.kotlin.methods.roomManage.playerManage.Player
import kotlin.coroutines.EmptyCoroutineContext

class Room (){
    val playerList = mutableListOf<Player>()
    var roomStatus:RoomStatus = RoomStatus.BeforeGame

    init {
        CoroutineScope(EmptyCoroutineContext).launch {
            println("AAAA")
        }
    }

    fun smallBroadcast(message:String){
        for (x in playerList){
            x.session.remote.sendString(message)
        }
    }

    fun whileGame(){
        val send = MessageData(
            "GameStarted",
            playerList.map { mapOf(
                "players" to it.nick
            ) }
        )

        smallBroadcast(Gson().toJson(send))
    }
}