package main.kotlin.methods.coroutinesManage

data class Timeout(
    val func:()->Unit,
    var startTime:Long,
)

//var time = System.currentTimeMillis()