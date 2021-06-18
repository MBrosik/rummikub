package main.kotlin.methods.roomManage.roomGson

import main.kotlin.methods.roomManage.cardManage.Card

data class PlayerMessage(
    val boardCards:MutableList<Card>,
    val inHandCards:MutableList<Card>,
)
