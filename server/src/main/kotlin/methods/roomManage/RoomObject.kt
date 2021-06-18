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
            it.playerList.filterNotNull().size < 4 && it.roomStatus == RoomStatus.BeforeGame
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
        val index = room.playerList.indexOf(null);

        room.playerList[index] = Player(userData.session, parsedData["name"]!!)
        userData.roomClass = room


        /**
         * Odesłanie klientowi wiadomości o dodaniu do pokoju
         */
        val sendMess = MessageData("onAddedToRoom", mutableMapOf("aa" to "Zostałeś dodany do pokoju!"))
        userData.session.remote.sendString(Gson().toJson(sendMess))

        room.sendUsersList();


        /**
         * Sprawdzanie, czy pokój jest zapełniony
         */
        if (room.playerList.filterNotNull().size== 4) {
            room.startGame();
        }
    }

    fun removeFromRoom(userData: SessionStructure) {

        userData.roomClass!!.playerList.forEachIndexed { ind,it->
            if(it!!.session == userData.session){
                userData.roomClass!!.playerList[ind] = null;

                if(userData.roomClass!!.whoseTurn == ind){
                    userData.roomClass!!.ownerOfTheTurnEnded();
                }
            }
        }
        userData.roomClass!!.sendUsersList();
        roomList.removeIf { it.playerList.filterNotNull().isEmpty() }
    }
}