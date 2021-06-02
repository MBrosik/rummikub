package test

import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

data class Message(
    val type:String,
    val info:Any,
)
//interface Message{
//    val type:String;
////    val info:HashMap<String,Any>;
//    val info:String;
//}

//data class Message<T>(
//    val type:String,
//    val info:T,
//)

