package main.kotlin.methods.coroutinesManage

data class Interval(
    val func:()->Unit,
    var startTime:Long,
)
