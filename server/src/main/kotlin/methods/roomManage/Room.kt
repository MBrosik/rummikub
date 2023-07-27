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
import main.kotlin.methods.roomManage.playerManage.Player
import main.kotlin.methods.roomManage.roomData.cardArrangement
import main.kotlin.methods.roomManage.roomData.cardCheck
import main.kotlin.methods.roomManage.roomData.pointCheck
import main.kotlin.methods.roomManage.roomData.randomCards
import main.kotlin.methods.roomManage.roomGson.PlayerMessage
import main.kotlin.methods.sqlQuery
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.floor

class Room() {
    val playerList = mutableListOf<Player?>(null, null, null, null)
    var roomStatus: RoomStatus = RoomStatus.BeforeGame
    val allCards = mutableListOf<Card>()

    //    val availableCards = mutableListOf<Card>()
    val JSON = sqlQuery.select("SELECT * FROM Constants WHERE name='cards'")[0]["VAL"]!!
    var availableCards = Gson().fromJson(JSON, MutableList::class.java)
        .map { Gson().fromJson(Gson().toJson(it), Card::class.java) } as MutableList<Card>

    var whoseTurn = 0;
    val board = Board();
    var minZ1 = 12;
    var maxZ1 = 14;
    var maxX1 = 12;
    lateinit var corountine: CoroutineScope;

    init {
        // -------------------------
        // talia kart
        // -------------------------

        allCards.addAll(availableCards);

//        availableCards.forEach {
//            val card = Gson().toJson(
//                mapOf(
//                    "id" to it.ID,
//                    "name" to it.name,
//                    "color" to it.color
//                )
//            )
//            println(card);
//        }
    }

    fun smallBroadcast(message: String) {
        for (x in playerList) {
            try {
                x?.session?.remote?.sendString(message)
            } catch (e: Throwable) {
                println(e)
            }
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
                    val id = if (randomCards) {
                        floor(Math.random() * availableCards.size).toInt()
                    } else {
                        availableCards.size-1
                    }
                    val card = availableCards[id]
                    card.x = x
                    card.y = minZ1
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
                                        "cardsCount" to it1.CardsInHand.size,
                                        "first_move" to it1.firstMove,
                                    )
                                } else {
                                    null
                                }
                            },
                            "YourIndex" to ind,
                            Pair("turn", playerList[whoseTurn]!!.session == it.session),
                            "reason" to "przyczyna nowej rundy"
                        ),
                    )

                    try {
                        it.session.remote.sendString(Gson().toJson(send));
                    } catch (e: Throwable) {
                        println("Błąd z endpointem")
                        println(e)
                    }
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

            corountine = CoroutineScope(EmptyCoroutineContext);
            corountine.launch {
                delay(60000)


                if (playerList.filterNotNull().isNotEmpty()) {
                    board.curentCards.clear();

                    drawCard();

                    do {
                        whoseTurn++;
                        if (whoseTurn == 4) whoseTurn = 0;

                    } while (playerList[whoseTurn] == null)

                    gameTurn();
                }
            }

        } else {
            // ------------------
            // if winner exists
            // ------------------
            roomStatus = RoomStatus.GameEnded;

            playerList.forEach {

                if (it != null) {
                    println(it)
                    val send = MessageData(
                        "GameEnded",
                        mutableMapOf<String, Any>(
                            "winnerName" to winnerPlayer.nick,
                            "youAreWinner" to (it == winnerPlayer)
                        ),
                    )
                    try {
                        WebSocketObject.sessions[it.hashCode()]!!.roomClass = null
                    } catch (e: Throwable) {
                        println("Problem")
                        println(e)
                    }
                    try {
                        it.session.remote.sendString(Gson().toJson(send));
                    } catch (e: Throwable) {
                        println("Problem")
                        println(e)
                    }
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

            yloop@ for (y1 in (minZ1..maxZ1)) {
                for (x1 in (0..maxX1)) {
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

        val playerObject = playerList.find {
            if (it != null) {
                it.session == userData.session
            } else {
                false
            }
        }!!

        val parsedSendData = Gson().fromJson(Gson().toJson(sendData), PlayerMessage::class.java);
        if (userData.session != playerList[whoseTurn]!!.session) return;


        // -------------------------------
        // check if everyone has position
        // -------------------------------
        println(parsedSendData.boardCards.find { it.x == null || it.y == null } == null)


        // -----------------------------
        // array of added to hand cards
        // -----------------------------

        val changeInHandTableA = parsedSendData.inHandCards.filter {
            playerObject.CardsInHand.find { it1 ->
                it1.name == it.name && it1.color == it.color && it1.ID == it.ID
            } == null
        }

        // --------------------------------
        // array of removed from hand cards
        // ---------------------------------

        val changeInHandTableB = playerObject.CardsInHand.filter {
            parsedSendData.inHandCards.find { it1 ->
                it1.name == it.name && it1.color == it.color && it1.ID == it.ID
            } == null
        }

        var points: Int = 0;
        if (
            parsedSendData.boardCards.find { it.x == null || it.y == null } == null
            && changeInHandTableA.isEmpty()
            && changeInHandTableB.isNotEmpty()
            && parsedSendData.boardCards.isNotEmpty()
        ) {


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
            var cardPoints = 0;

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
                        if (searchTable.size in 1..2) {
                            everythingOK = false;
                            break;
                        } else if (searchTable.size >= 3) {

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
                                        if (searchTable[nextIndex].name != "joker") {
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

                                    // ------------------
                                    // check 30 points
                                    // ------------------
                                    val filterArr1 =
                                        searchTable.filter { changeInHandTableB.find { it1 -> it1.ID == it.ID } != null }

                                    if (filterArr1.size == searchTable.size) {
                                        val preFirstCard = searchTable[preFirstIndex];
//                                        cardPoints = 0;
                                        if (type == cardArrangement.byNumbers) {
                                            val zeroValue = preFirstCard.name.toInt() - preFirstIndex;

                                            for (x in (0 until searchTable.size)) {
                                                cardPoints += zeroValue + x;
                                            }

                                        } else if (type == cardArrangement.byColors) {
                                            cardPoints += preFirstCard.name.toInt() * searchTable.size;
                                        }
                                    }


                                }
                            }

                            searchTable.clear()
                            continue;
                        }
                    }
                    if (card != null) {
                        searchTable.add(card)
                    }
                }

                if (!everythingOK) break;
            }
            if (
                (
                        everythingOK
                                && (cardPoints >= 30 || !playerObject.firstMove || !pointCheck)
                        )
                || !cardCheck
            ) {
                board.cards = parsedSendData.boardCards
                playerObject.CardsInHand = parsedSendData.inHandCards
                playerObject.firstMove = false;
            } else if (availableCards.size != 0) {
                val arr1 = parsedSendData.inHandCards.toMutableList();

                val change2 = changeInHandTableB.toList();

                change2.forEach {
                    var x: Int = 0;
                    var y: Int = 0;

                    yloop@ for (y1 in (minZ1..maxZ1)) {
                        for (x1 in (0..maxX1)) {
                            val tempCard = arr1.find { it.x == x1 && it.y == y1 }

                            if (tempCard == null) {
                                x = x1;
                                y = y1;

                                break@yloop
                            }
                        }
                    }

                    it.x = x
                    it.y = y

                    arr1.add(it);
                }

                playerObject.CardsInHand = arr1;

                drawCard()
            }

        } else if (availableCards.size != 0) {
            val arr1 = parsedSendData.inHandCards.toMutableList();

            val change2 = changeInHandTableB.toList();

            change2.forEach {
                var x: Int = 0;
                var y: Int = 0;

                yloop@ for (y1 in (minZ1..maxZ1)) {
                    for (x1 in (0..maxX1)) {
                        val tempCard = arr1.find { it.x == x1 && it.y == y1 }

                        if (tempCard == null) {
                            x = x1;
                            y = y1;

                            break@yloop
                        }
                    }
                }

                it.x = x
                it.y = y

                arr1.add(it);
            }

            playerObject.CardsInHand = arr1;
            drawCard() // if the player took a card from the board or has not added anything to the board
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