package main.kotlin.methods.roomManage

import main.kotlin.methods.roomManage.cardManage.Card

class Board {
    var size= 70;
    var cards = mutableListOf<Card>();
    val curentCards = mutableListOf<Card>();
}