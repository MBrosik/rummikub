package main.kotlin.methods.WebSocket

import main.kotlin.methods.roomManage.Room
import org.eclipse.jetty.websocket.api.Session


data class SessionStructure(
    val session:Session,
    var roomClass:Room? = null
)
