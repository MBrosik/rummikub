package main.kotlin.methods.roomManage

import com.google.gson.Gson
import main.kotlin.methods.WebSocket.MessageData
import main.kotlin.methods.WebSocket.SessionStructure
import main.kotlin.methods.roomManage.playerManage.Player

object RoomObject {
    val roomList = mutableListOf<Room>()

    fun searchForRoom(userData: SessionStructure, messageData: Any) {
        if (userData.roomClass != null) return

        /**
         * rzutowanie danych
         */
        val parsedData = messageData as MutableMap<String, String>


        /**
         * Wyszukiwanie wolnych pokoi
         */
        val availableRooms = roomList.filter {
            it.playerList.size < 4 && it.roomStatus == RoomStatus.BeforeGame
        }

        val room: Room;
        if (availableRooms.isEmpty()) {
            room = Room()
            roomList.add(room)
        } else {
            room = availableRooms[0];
        }


        /**
         * Dodanie gracza do pokoju
         */
        room.playerList.add(Player(userData.session, parsedData["name"]!!))
        userData.roomClass = room

        println("----------------")
        println("Pokoje:")
        println("----------------")
        for (x in roomList) {
            println(x.playerList)
        }

        /**
         * Sprawdzanie, czy pokój jest zapełniony
         */
        if (room.playerList.size == 4) {
            room.roomStatus = RoomStatus.WhileGame;
            room.whileGame();
        }


        /**
         * Odesłanie klientowi wiadomości o dodaniu do pokoju
         */
        val sendMess = MessageData("onAddedToRoom", "Zostałeś dodany do pokoju!")
        userData.session.remote.sendString(Gson().toJson(sendMess))
    }

    fun removeFromRoom(userData: SessionStructure) {
        userData.roomClass!!.playerList.removeIf { it.session == userData.session }
        roomList.removeIf { it.playerList.size == 0 }
    }
}