//package main.kotlin.test
//
//import main.kotlin.methods.roomManage.cardManage.Card
//import main.kotlin.methods.roomManage.roomData.cardArrangement
//
//fun main() {
//
//    // -----------------
//    // set min max
//    // -----------------
//    val xTable = parsedSendData.cards.map { it.x!!.toDouble() }.sorted();
//    val yTable = parsedSendData.cards.map { it.y!!.toDouble() }.sorted();
//
//    val minX = xTable[0].toInt();
//    val maxX = xTable[xTable.size - 1].toInt();
//
//    val minY = yTable[0].toInt();
//    val maxY = yTable[yTable.size - 1].toInt();
//
//
//    // ----------------
//    // check for cards
//    // ----------------
//    val searchTable = mutableListOf<Card>();
//
//    var everythingOK = true;
//
//    for (y in (minY..maxY)) {
//        searchTable.clear()
//        for (x in (minX..maxX)) {
//            // ----------------
//            // check if null
//            // ----------------
//            val card = parsedSendData.cards.find { it.x == x && it.y == y }
//            if (card == null) {
//                if (searchTable.size < 3) {
//                    everythingOK = false;
//                    break;
//                } else {
//                    searchTable.clear()
//                    continue;
//                }
//            }
//
//
//            if (card.name != "Joker" && searchTable.size != 0) {
//                val last = searchTable[searchTable.size - 1];
//
//                if (
//                    searchTable.size == 1
//                    && !(
//                            card.name.toInt() - last.name.toInt() == 1
//                                    && card.color == last.color
//                            )
//                    && !(
//                            card.name.toInt() == last.name.toInt()
//                                    && card.color != last.color
//                            )
//                ) {
//                    everythingOK = false;
//                    break;
//                } else {
//                    val firstCard = searchTable.find { it.name != "Joker" }
//
//                    if (firstCard != null) {
//                        val firstIndex = searchTable.indexOf(firstCard);
//
//                        if(firstIndex == searchTable.size){
//                            if (
//                                !(
//                                        card.name.toInt() - last.name.toInt() == 1
//                                                && card.color == last.color
//                                        )
//                                && !(
//                                        card.name.toInt() == last.name.toInt()
//                                                && card.color != last.color
//                                        )
//                            ) {
//                                everythingOK = false;
//                                break;
//                            }
//                        }
//                    }
////                            val type = if (searchTable[0].name)
//                }
//            }
//
//            searchTable.add(card)
//        }
//        if (!everythingOK) break;
//
//
//        //--------------------------------------------------------------------------------------------------
//        //--------------------------------------------------------------------------------------------------
//        //--------------------------------------------------------------------------------------------------
//        //--------------------------------------------------------------------------------------------------
//        //--------------------------------------------------------------------------------------------------
//        //--------------------------------------------------------------------------------------------------
//        //--------------------------------------------------------------------------------------------------
//        //--------------------------------------------------------------------------------------------------
//        //--------------------------------------------------------------------------------------------------
//        //--------------------------------------------------------------------------------------------------
//        //--------------------------------------------------------------------------------------------------
//        //--------------------------------------------------------------------------------------------------
//        //--------------------------------------------------------------------------------------------------
//        //--------------------------------------------------------------------------------------------------
//        //--------------------------------------------------------------------------------------------------
//        //--------------------------------------------------------------------------------------------------
//        //--------------------------------------------------------------------------------------------------
//        //--------------------------------------------------------------------------------------------------
//
//
//
//        while (true) {
//            val nextCard:Card? = searchTable[nextIndex];
//
//            if(nextCard == null) break;
//
//            if (nextCard.name == "Joker") {
//                nextIndex++;
//                nextNumber++;
//            } else {
//                if (
//                    nextCard.name == nextNumber.toString()
//                    && nextCard.color == firstCard.color
////                ) {
//                    type = cardArrangement.byNumbers
//                } else if (
//                    nextCard.name == firstCard.name
//                    && nextCard.color != firstCard.color
//                ) {
//                    type = cardArrangement.byColors
//                }
//
//                if(type == null){
//                    everythingOK = false
//                }
//
//                break
//            }
//
//        }
//
//    }
//}



//        // -----------
//        // board
//        // -----------
//        val changeBoardTableA = parsedSendData.boardCards.toMutableList() // if added to board
//        changeBoardTableA.removeAll(board.cards)
//
//        val changeBoardTableB = board.cards.toMutableList() // if removed from board
//        changeBoardTableB.removeAll(parsedSendData.boardCards)
//
//
//        // -----------
//        // in hand
//        // -----------
//        val changeInHandTableA = parsedSendData.inHandCards.toMutableList() // if added to hand
//        changeInHandTableA.removeAll(playerObject.CardsInHand)
//
//        val changeInHandTableB = playerObject.CardsInHand.toMutableList() // if removed from hand
//        changeInHandTableB.removeAll(parsedSendData.inHandCards)
//
//        val notAddedCard = changeBoardTableA.toMutableList();
//        notAddedCard.removeAll()
