package main.kotlin.methods.roomManage.cardManage

data class Card(
    var ID:Int,
    var name:String,
    var color:colorTypes,

    var x:Int? = null,
    var y:Int? = null,
)