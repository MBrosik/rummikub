package main.kotlin.methods.roomManage.roomGson

import main.kotlin.methods.roomManage.cardManage.Card

data class PlayerFinishedTurn(
    val cards:MutableList<Card>
)
