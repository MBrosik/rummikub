package main.kotlin.methods.roomManage.playerManage

import main.kotlin.methods.roomManage.cardManage.Card
import org.eclipse.jetty.websocket.api.Session

class Player(
    val session: Session,
    val nick:String
){
    var CardsInHand = mutableListOf<Card>()
    var firstMove = true;
}