package main.kotlin.methods.roomManage

import main.kotlin.methods.roomManage.playerManage.Player

class Room (){
    val playerList = mutableListOf<Player>()
    var roomStatus:RoomStatus = RoomStatus.BeforeGame

    init {

    }

    fun whileGame(){

    }
}