package main.kotlin.methods.roomManage

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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

    fun whileGame(){

    }
}