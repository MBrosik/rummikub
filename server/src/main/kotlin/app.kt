package main.kotlin

import main.kotlin.methods.WebSocket.WebSocketObject
import main.kotlin.methods.sqlQuery
import spark.Request
import spark.Response
import spark.Spark
import spark.Spark.get
import spark.Spark.webSocket
import spark.kotlin.Http
import spark.kotlin.ignite

fun main() {
    println("Hello world");

    sqlQuery.execute("CREATE TABLE IF NOT EXISTS Rooms(" +
            "id int primary key auto_increment, " +
            "imie varchar(255) not null" +
            ");")



    /**
     * websocket
     */
    webSocket("/rummikub", WebSocketObject.WebSocketServer::class.java)



    /**
     * spark
     */

    val http: Http = ignite()
    with(http) {
        /**
         * server config
         */

        Spark.staticFileLocation("/public")
        Spark.port(getHerokuPort())

        Spark.before({ request, response ->
            response.header("Access-Control-Allow-Methods", "*")
            response.header("Access-Control-Allow-Origin", "*")
        })

        /**
         * routes
         */
        get("/") { request, response -> mainPageGet(request, response) }
    }
}


fun mainPageGet(request:Request, response:Response):String{
    response.type("application/json");
    println("ee")

    sqlQuery.execute("INSERT INTO Rooms (imie) VALUES ('rre');")

    val temp = sqlQuery.select("SELECT id, imie FROM Rooms;")
    println(temp)

    return "{'a':1}";
}

fun getHerokuPort(): Int {
    val processBuilder = ProcessBuilder()
    return if (processBuilder.environment()["PORT"] != null) {
        processBuilder.environment()["PORT"]!!.toInt()
    } else 5000
}


fun xD(){

}