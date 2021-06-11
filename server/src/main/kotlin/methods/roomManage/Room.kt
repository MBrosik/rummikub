package main.kotlin.methods.roomManage

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import main.kotlin.methods.WebSocket.MessageData
import main.kotlin.methods.roomManage.cardManage.Card
import main.kotlin.methods.roomManage.cardManage.colorTypes
import main.kotlin.methods.roomManage.playerManage.Player
import kotlin.coroutines.EmptyCoroutineContext

class Room() {
    val playerList = mutableListOf<Player>()
    var roomStatus: RoomStatus = RoomStatus.BeforeGame

    val availableCards = mutableListOf<Card>()

    init {
        var id = 0;
        for (x in (1..13)) {
            for (x in colorTypes.values()) {
                availableCards.add(Card(id, x.toString(), x))
                id++;
            }
        }

        listOf<colorTypes>(colorTypes.black, colorTypes.red).forEach {
            availableCards.add(Card(id, "Joker", it))
        }

        availableCards.forEach {
            val card = Gson().toJson(
                mapOf(
                    "name" to it.name,
                    "color" to it.color
                )
            )
            println(card);
        }

        CoroutineScope(EmptyCoroutineContext).launch {
            println("AAAA")
        }
    }

    fun smallBroadcast(message: String) {
        for (x in playerList) {
            x.session.remote.sendString(message)
        }
    }

    fun whileGame() {
        val send = MessageData(
            "GameStarted",
            playerList.map {
                mapOf(
                    "players" to it.nick
                )
            }
        )

        smallBroadcast(Gson().toJson(send))
    }
}