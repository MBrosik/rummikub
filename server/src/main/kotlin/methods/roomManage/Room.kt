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
    val playerList = mutableListOf<Player?>(null, null, null, null)
    var roomStatus: RoomStatus = RoomStatus.BeforeGame
    val allCards = mutableListOf<Card>()
    val availableCards = mutableListOf<Card>()
//    val JSON = sqlQuery.select("SELECT * FROM Constants WHERE key='cards'")[0]["cards"]!!
//    val availableCards = Gson().fromJson(JSON, MutableList::class.java) as MutableList<Card>

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
                availableCards.add(Card(id, "joker", it))
                id++
            }
        }

        println(Gson().toJson(availableCards));

        allCards.addAll(availableCards);

        availableCards.forEach {
            val card = Gson().toJson(
                mapOf(
                    "id" to it.ID,
                    "name" to it.name,
                    "color" to it.color
                )
            )
            println(card);
        }
    }

    fun smallBroadcast(message: String) {
        for (x in playerList) {
            x?.session?.remote?.sendString(message)
        }
    }

    fun sendUsersList() {
        val msg = MessageData(
            "players_list", mapOf(
                "playerList" to playerList.map {
                    if (it != null) {
                        mapOf(
                            "name" to it?.nick
                        )
                    } else {
                        null;
                    }
                },
            )
        )
        smallBroadcast(Gson().toJson(msg));
    }

    fun startGame() {
        // -----------
        // close room
        // -----------
        roomStatus = RoomStatus.WhileGame;


        // --------------
        // drawing Cards
        // --------------
//        val sendMap = mutableMapOf<String, Any>(
//            "playerList" to playerList.map(fun(it): Map<String, String>? {
//                if (it != null) {
//                    return mapOf(
//                        "players" to it?.nick
//                    )
//                } else {
//                    return null;
//                }
//            }),
//            "whoseTurn" to whoseTurn
//        )
        val sendMap = mutableMapOf<String, Any>(
            "playerList" to playerList.map {
                if (it != null) {
                    mapOf(
                        "name" to it?.nick
                    )
                } else {
                    null;
                }
            },
            "whoseTurn" to whoseTurn
        )



        playerList.forEachIndexed { ind, it ->
            if (it != null) {

                for (x in (0..10)) {
                    val id = floor(Math.random() * availableCards.size).toInt()
                    val card = availableCards[id]
                    card.x = x
                    card.y = 12
                    it.CardsInHand.add(card)
                    availableCards.removeAt(id);
                }


                // ---------------------
                // send info to client
                // ---------------------
                sendMap["drawnCard"] = it.CardsInHand;
                sendMap["YourIndex"] = ind;

                val send = MessageData(
                    "GameStarted",
                    sendMap,
                )

                it.session.remote.sendString(Gson().toJson(send));
            }
        }

        println("Talia kart: " + availableCards.size)

        gameTurn()
    }


    fun gameTurn() {


        // ------------------
        // check for winner
        // ------------------
        val winnerPlayer = playerList.find {
            if (it != null) {
                it.CardsInHand.size == 0
            } else {
                false
            }
        }


        // ------------------
        // if winner is null
        // ------------------
        if (winnerPlayer == null) {
            println("Not winner");
            playerList.forEachIndexed() { ind, it ->
                if (it != null) {

                    val send = MessageData(
                        "WhileGame",
                        mutableMapOf<String, Any>(
                            "boardCards" to board.cards,
                            "inHandCards" to it.CardsInHand,
                            "whoseTurn" to whoseTurn,
                            "playerlist" to playerList.map { it1 ->
                                if (it1 != null) {
                                    mapOf(
                                        "name" to it1.nick,
                                        "cardsCount" to it1.CardsInHand.size
                                    )
                                } else {
                                    null
                                }
                            },
                            "YourIndex" to ind,
                            Pair("turn", playerList[whoseTurn]!!.session == it.session)
                        ),
                    )
                    it.session.remote.sendString(Gson().toJson(send));
                }
            }

            // ---------------------
            // launch Corounitine
            // ---------------------

            try {
                corountine.cancel();
            } catch (e: Throwable) {
                println(" Corutyna nie istnieje$e")
            }

            println("Po Corountine")
            corountine = CoroutineScope(EmptyCoroutineContext);
            corountine.launch {
                println("Po launch")
                delay(60000)
                println("Po delay")


//                if (playerList.filter { it != null }.size != 0) {
//                if (playerList.filterNotNull().size != 0) {
                if (playerList.filterNotNull().isNotEmpty()) {

                    println("Po ifie")

                    board.curentCards.clear();

//                    if (availableCards.size != 0) {
//                        val id = floor(Math.random() * availableCards.size).toInt()
//                        playerList[whoseTurn]!!.CardsInHand.add(availableCards[id])
//                        availableCards.removeAt(id)
//                    }

                    drawCard();

                    do {
                        whoseTurn++;
                        if (whoseTurn == 4) whoseTurn = 0;

                    } while (playerList[whoseTurn] == null)

                    gameTurn();
                }
            }

        } else {
            println("Fucking winner!!!")
            // ------------------
            // if winner exists
            // ------------------
            roomStatus = RoomStatus.GameEnded;

            playerList.forEach {

                if (it != null) {
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

    fun drawCard() {
        if (availableCards.size != 0) {

            // -----------------
            // set min max
            // -----------------

            var x: Int = 0;
            var y: Int = 0;

            yloop@ for (y1 in (12..14)) {
                for (x1 in (0..12)) {
                    val tempCard = playerList[whoseTurn]!!.CardsInHand.find { it.x == x1 && it.y == y1 }

                    if (tempCard == null) {
                        x = x1;
                        y = y1;

                        break@yloop
                    }
                }
            }


            val id = floor(Math.random() * availableCards.size).toInt()
            val card = availableCards[id]


            card.x = x
            card.y = y


            playerList[whoseTurn]!!.CardsInHand.add(card);
            availableCards.removeAt(id)
        }
    }

    fun playerFinishedTurn(userData: SessionStructure, sendData: Any) {
        val playerObject = playerList.find { it!!.session == userData.session }!!
        println(sendData);
        println(Gson().toJson(sendData))

        val parsedSendData = Gson().fromJson(Gson().toJson(sendData), PlayerMessage::class.java);
        if (userData.session != playerList[whoseTurn]!!.session) return;


        // -------------------------------
        // check if everyone has position
        // -------------------------------
        println("");
        println("Found Card:")
        println(parsedSendData.boardCards.find { it.x == null || it.y == null } == null)


        // -------------------
        // if added to hand
        // -------------------

        val changeInHandTableA = parsedSendData.inHandCards.filter {
            playerObject.CardsInHand.find { it1 ->
                it1.name == it.name && it1.color == it.color
            } == null
        }

        // -----------------------
        // if removed from hand
        // -----------------------

        val changeInHandTableB = playerObject.CardsInHand.filter {
            parsedSendData.inHandCards.find { it1 ->
                it1.name == it.name && it1.color == it.color
            } == null
        }

        var points: Int = 0;
        if (
            parsedSendData.boardCards.find { it.x == null || it.y == null } == null
//            && changeInHandTableA.isEmpty()
//            && changeInHandTableB.isNotEmpty()
            && parsedSendData.boardCards.isNotEmpty()
        ) {

            println("after if 345 line")


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

                    println("W forze")
//                    println("${}")

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
                            // check if all joker
                            // ---------------------
                            var firstCard = searchTable.find { it.name != "joker" }

                            if (firstCard != null) {
                                var firstIndex = searchTable.indexOf(firstCard);
                                val preFirstIndex = firstIndex;

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

                                        if (nextCard.name == "joker") {
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
                                        // check if next Card isn't joker
                                        // --------------------------------
                                        if (nextCard.name != "joker") {

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

                                    if (type == cardArrangement.byNumbers) {

                                    } else if (type == cardArrangement.byColors) {
//                                        searchTable.
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

            } else if (availableCards.size != 0) {
//                val id = floor(Math.random() * availableCards.size).toInt()
//                playerList[whoseTurn]!!.CardsInHand.add(availableCards[id])
//                availableCards.removeAt(id)
                drawCard()
            }

        }
        //else if(
        //changeInHandTableA.isNotEmpty()
        //|| changeInHandTableB.isEmpty()
        //){
        else if (availableCards.size != 0) {
            // if the player took a card from the board or has not added anything to the board
//            val id = floor(Math.random() * availableCards.size).toInt()
//            playerList[whoseTurn]!!.CardsInHand.add(availableCards[id])
//            availableCards.removeAt(id)
            drawCard()
        }
        board.curentCards.clear();

        corountine.cancel();

        if (playerList.filterNotNull().isEmpty()) return

        do {
            whoseTurn++;
            if (whoseTurn == 4) whoseTurn = 0;

        } while (playerList[whoseTurn] == null)

        gameTurn();
    }

    fun ownerOfTheTurnEnded() {

        if (playerList.filterNotNull().isEmpty()) return

        do {
            whoseTurn++;
            if (whoseTurn == 4) whoseTurn = 0;

        } while (playerList[whoseTurn] == null)
        corountine.cancel();
        gameTurn();
    }
}