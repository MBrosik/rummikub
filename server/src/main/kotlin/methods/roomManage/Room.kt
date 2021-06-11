package main.kotlin.methods.roomManage

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import main.kotlin.methods.WebSocket.MessageData
import main.kotlin.methods.WebSocket.SessionStructure
import main.kotlin.methods.roomManage.cardManage.Card
import main.kotlin.methods.roomManage.cardManage.colorTypes
import main.kotlin.methods.roomManage.playerManage.Player
import main.kotlin.methods.roomManage.roomGson.PlayerFinishedTurn
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.floor

class Room() {
    val playerList = mutableListOf<Player>()
    var roomStatus: RoomStatus = RoomStatus.BeforeGame
    val availableCards = mutableListOf<Card>()

    var whoseTurn = 0;
    val board = Board();
    lateinit var corountine: CoroutineScope;

    init {
        // -------------------------
        // talia kart
        // -------------------------
        var id = 0;
        for (z in (0..1)) {
            for (x in (1..13)) {
                for (y in colorTypes.values()) {
                    availableCards.add(Card(id, x.toString(), y))
                    id++;
                }
            }

            listOf<colorTypes>(colorTypes.black, colorTypes.red).forEach {
                availableCards.add(Card(id, "Joker", it))
                id++
            }
        }

        availableCards.forEach {
            val card = Gson().toJson(
                mapOf(
                    "idd" to it.ID,
                    "name" to it.name,
                    "color" to it.color
                )
            )
            println(card);
        }
    }

    fun smallBroadcast(message: String) {
        for (x in playerList) {
            x.session.remote.sendString(message)
        }
    }

    fun startGame() {
        // -----------
        // close room
        // -----------
        roomStatus = RoomStatus.WhileGame;


        // --------------
        // drawing Cards
        // --------------
        val sendMap = mutableMapOf<String, Any>(
            "playerList" to playerList.map {
                mapOf(
                    "players" to it.nick
                )
            },
            "whoseTurn" to whoseTurn
        )



        playerList.forEach {
            for (x in (0..10)) {
                val id = floor(Math.random() * availableCards.size).toInt()
                it.CardsInHand.add(availableCards[id])
                availableCards.removeAt(id);
            }


            // ---------------------
            // send info to client
            // ---------------------
            sendMap["drawnCard"] = it.CardsInHand;
            val send = MessageData(
                "GameStarted",
                sendMap,
            )

            it.session.remote.sendString(Gson().toJson(send));
        }

        println("Talia kart: " + availableCards.size)
    }


    fun gameTurn() {
        corountine = CoroutineScope(EmptyCoroutineContext);
        corountine.launch {
            delay(60000)

            board.curentCards.clear();
        }

        corountine.cancel();
    }

    fun playerFinishedTurn(userData: SessionStructure, sendData: Any) {
        val parsedSendData = sendData as PlayerFinishedTurn;


        if (parsedSendData.cards.find { it.x == null || it.y == null } != null) {

            // -----------------
            // set min max
            // -----------------
            val xTable = parsedSendData.cards.map { it.x!!.toDouble() }.sorted();
            val yTable = parsedSendData.cards.map { it.y!!.toDouble() }.sorted();

            val minX = xTable[0].toInt();
            val maxX = xTable[xTable.size - 1].toInt();

            val minY = yTable[0].toInt();
            val maxY = yTable[yTable.size - 1].toInt();


            // ----------------
            // check for cards
            // ----------------
            val searchTable = mutableListOf<Card>();

            var everythingOK = true;

            for (y in (minY..maxY)) {
                searchTable.clear()
                for (x in (minX..maxX)) {
                    // ----------------
                    // check if null
                    // ----------------
                    val card = parsedSendData.cards.find { it.x == x && it.y == y }
                    if (card == null) {
                        if (searchTable.size < 3) {
                            everythingOK = false;
                            break;
                        } else {
                            searchTable.clear()
                            continue;
                        }
                    }


                    if (card.name != "Joker" && searchTable.size != 0) {
                        val last = searchTable[searchTable.size - 1];

                        if (
                            searchTable.size == 1
                            && !(
                                    card.name.toInt() - last.name.toInt() == 1
                                            && card.color == last.color
                                    )
                            && !(
                                    card.name.toInt() == last.name.toInt()
                                            && card.color != last.color
                                    )
                        ) {
                            everythingOK = false;
                            break;
                        } else {
                            val firstCard = searchTable.find { it.name != "Joker" }

                            if (firstCard != null) {
                                val firstIndex = searchTable.indexOf(firstCard);

                                if(firstIndex == searchTable.size){
                                    if (
                                        !(
                                                card.name.toInt() - last.name.toInt() == 1
                                                        && card.color == last.color
                                                )
                                        && !(
                                                card.name.toInt() == last.name.toInt()
                                                        && card.color != last.color
                                                )
                                    ) {
                                        everythingOK = false;
                                        break;
                                    }
                                }
                            }
//                            val type = if (searchTable[0].name)
                        }
                    }

                    searchTable.add(card)
                }
                if (!everythingOK) break;
            }
        }
        board.curentCards.clear();
    }
}