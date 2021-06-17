//package main.kotlin.test
//
//fun main() {
//    if (
//        searchTable.size == 1
//        && searchTable[0].name != "Joker"
//    ) {
//        if (
//            !(
//                    card.name.toInt() - searchTable[0].name.toInt() == 1
//                            && card.color == searchTable[0].color
//                    )
//            && !(
//                    card.name.toInt() == searchTable[0].name.toInt()
//                            && card.color != searchTable[0].color
//                    )
//        ) {
//
//            everythingOK = false;
//            break;
//        }
//    } else if (
//        searchTable.size > 1
//        && searchTable[searchTable.size - 1].name != "Joker"
//    ) {
//        val last = searchTable[searchTable.size - 1];
//        val penultimate =searchTable[searchTable.size - 2]
//
//        val samecolor =  (card.color == last.color) && (last.color == penultimate.color);
//        val samenumber =  (card.name == last.name) && (last.name == penultimate.name);
//
//
//        if (
//            (last-penultimate == 1 && card.name.toInt() - last == 1)
//
//        ) {
//
//        }
//
//    }
//}