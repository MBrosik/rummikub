package methods.WebSocket

//interface Message{
//    val type:String;
////    val info:HashMap<String,Any>;
//    val info:String;
//}

data class Message<T>(
    val type:String,
    val info:T,
)
