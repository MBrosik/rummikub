package main.kotlin.methods.roomManage

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import main.kotlin.methods.WebSocket.MessageData
import main.kotlin.methods.WebSocket.SessionStructure
import main.kotlin.methods.WebSocket.WebSocketObject
import main.kotlin.methods.roomManage.cardManage.Card
import main.kotlin.methods.roomManage.cardManage.colorTypes
import main.kotlin.methods.roomManage.playerManage.Player
import main.kotlin.methods.roomManage.roomData.cardArrangement
import main.kotlin.methods.roomManage.roomGson.PlayerMessage
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.floor

class Room() {
    val playerList = mutableListOf<Player>()
    var roomStatus: RoomStatus = RoomStatus.BeforeGame
    val allCards = mutableListOf<Card>()
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
        allCards.addAll(availableCards);

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

        gameTurn()
    }


    fun gameTurn() {
        corountine = CoroutineScope(EmptyCoroutineContext);
        corountine.launch {
            val winnerPlayer = playerList.find { it.CardsInHand.size == 0 }

            if (winnerPlayer == null) {
                playerList.forEach {
                    val send = MessageData(
                        "WhileGame",
                        mutableMapOf<String, Any>(
                            "boardCards" to board.cards,
                            "inHandCards" to it.CardsInHand,
                            "whoseTurn" to whoseTurn,
                            Pair("turn", playerList[whoseTurn].session == it.session)
                        ),
                    )
                    it.session.remote.sendString(Gson().toJson(send));
                }

                delay(60000)
                board.curentCards.clear();

                whoseTurn++;
                if (whoseTurn == 4) whoseTurn = 0;
                gameTurn();
            }
            else{
                roomStatus = RoomStatus.GameEnded;

                playerList.forEach {
                    val send = MessageData(
                        "GameEnded",
                        mutableMapOf<String, Any>(
                            "winnerName" to winnerPlayer.nick,
                            "youAreWinner" to (it == winnerPlayer)
                        ),
                    )
                    WebSocketObject.sessions[it.hashCode()]!!.roomClass = null
                    it.session.remote.sendString(Gson().toJson(send));
                }
            }
        }
    }

    fun playerFinishedTurn(userData: SessionStructure, sendData: Any) {
        val playerObject = playerList.find { it.session == userData.session }!!
        println(sendData);

        val parsedSendData = Gson().fromJson(Gson().toJson(sendData), PlayerMessage::class.java);
        if (userData.session != playerList[whoseTurn].session) return;


        // -------------------------------
        // check if everyone has position
        // -------------------------------
        println("");
        println("Found Card:")
        println(parsedSendData.boardCards.find { it.x == null || it.y == null } == null)


        if (parsedSendData.boardCards.find { it.x == null || it.y == null } == null) {


            // -----------------
            // set min max
            // -----------------
            val xTable = parsedSendData.boardCards.map { it.x!!.toDouble() }.sorted();
            val yTable = parsedSendData.boardCards.map { it.y!!.toDouble() }.sorted();

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

                    if (!everythingOK) break

                    // ----------------
                    // check if null
                    // ----------------
                    val card = parsedSendData.boardCards.find { it.x == x && it.y == y }

                    if (card != null && x == maxX) {
                        searchTable.add(card)
                    }

                    if (card == null || x == maxX) {

                        // ---------------------
                        // check table size
                        // ---------------------
                        if (searchTable.size < 3) {
                            everythingOK = false;
                            break;
                        } else {

                            // ---------------------
                            // check if all Joker
                            // ---------------------
                            var firstCard = searchTable.find { it.name != "Joker" }

                            if (firstCard != null) {
                                var firstIndex = searchTable.indexOf(firstCard);

                                // ------------------------------
                                // check if firstCard is not last
                                // ------------------------------
                                if (firstIndex != searchTable.size - 1) {
                                    var nextIndex = firstIndex + 1;
                                    var nextNumber = firstCard.name.toInt() + 1
                                    var type: cardArrangement? = null;
                                    val takenColors = mutableListOf<String>()

                                    // --------------
                                    // Check type
                                    // --------------
                                    while (nextIndex < searchTable.size) {
                                        val nextCard = searchTable[nextIndex];

                                        if (nextCard.name == "Joker") {
                                            nextIndex++;
                                            nextNumber++;
                                        } else {

                                            // ------------------------------
                                            // set by Numbers cardArrangement
                                            // ------------------------------
                                            if (
                                                nextCard.name == nextNumber.toString()
                                                && nextCard.color == firstCard.color
                                            ) {
                                                type = cardArrangement.byNumbers
                                            }

                                            // ------------------------------
                                            // set by Colors cardArrangement
                                            // ------------------------------
                                            else if (
                                                nextCard.name == firstCard.name
                                                && nextCard.color != firstCard.color
                                            ) {
                                                type = cardArrangement.byColors

                                                takenColors.add(nextCard.color.toString())
                                                takenColors.add(firstCard.color.toString())
                                            }

                                            if (type == null) {
                                                everythingOK = false
                                            }

                                            break
                                        }

                                    }

                                    // -------------------
                                    // check next Cards
                                    // -------------------


                                    while (
                                        type != null
                                        && nextIndex < searchTable.size - 1
                                    ) {
                                        // -------------------
                                        // set First card
                                        // -------------------
                                        if (searchTable[nextIndex].name != "Joker") {
                                            firstIndex = nextIndex;
                                            firstCard = searchTable[firstIndex]
                                        }

                                        // -------------------
                                        // set next Card
                                        // -------------------
                                        nextNumber++;
                                        nextIndex++;
                                        val nextCard = searchTable[nextIndex];

                                        // --------------------------------
                                        // check if next Card isn't Joker
                                        // --------------------------------
                                        if (nextCard.name != "Joker") {

                                            // --------------------------------
                                            // if cardArrangement by Numbers
                                            // --------------------------------
                                            if (
                                                type == cardArrangement.byNumbers

                                                && !(nextCard.name == nextNumber.toString()
                                                        && nextCard.color == firstCard!!.color)
                                            ) {
                                                everythingOK = false;
                                                break
                                            }

                                            // --------------------------------
                                            // if cardArrangement by Colors
                                            // --------------------------------
                                            if (
                                                type == cardArrangement.byColors
                                            ) {
                                                if (
                                                    nextCard.name == firstCard!!.name
                                                    && nextCard.color != firstCard!!.color
                                                    && takenColors.find { it == nextCard.color.toString() } == null // check if color not exists
                                                ) {
                                                    takenColors.add(nextCard.color.toString())
                                                } else {
                                                    everythingOK = false;
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            searchTable.clear()
                            continue;
                        }
                    }
                    searchTable.add(card)
                }

                if (!everythingOK) break;
            }

            if (everythingOK) {
                board.cards = parsedSendData.boardCards
                playerObject.CardsInHand = parsedSendData.inHandCards
            }
        }
        board.curentCards.clear();
        whoseTurn++;
        if (whoseTurn == 4) whoseTurn = 0;

        corountine.cancel();

        gameTurn();
    }
}