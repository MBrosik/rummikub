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

//    sqlQuery.execute(
//        "CREATE TABLE IF NOT EXISTS Rooms(" +
//                "id int primary key auto_increment, " +
//                "imie varchar(255) not null" +
//                ");"
//    )


    sqlQuery.execute(
        "CREATE TABLE IF NOT EXISTS Constants(" +
                "id int primary key auto_increment, " +
                "key varchar(255) not null," +
                "value text not null"+
                ");"
    )

    val valueTable = sqlQuery.select("SELECT * FROM Constants")

    if(valueTable.size == 0){
        sqlQuery.execute("INSERT INTO Constants (key, value) VALUES ('cards', '[{\"name\":\"1\",\"color\":\"red\"},{\"name\":\"1\",\"color\":\"orange\"},{\"name\":\"1\",\"color\":\"black\"},{\"name\":\"1\",\"color\":\"blue\"},{\"name\":\"2\",\"color\":\"red\"},{\"name\":\"2\",\"color\":\"orange\"},{\"name\":\"2\",\"color\":\"black\"},{\"name\":\"2\",\"color\":\"blue\"},{\"name\":\"3\",\"color\":\"red\"},{\"name\":\"3\",\"color\":\"orange\"},{\"name\":\"3\",\"color\":\"black\"},{\"name\":\"3\",\"color\":\"blue\"},{\"name\":\"4\",\"color\":\"red\"},{\"name\":\"4\",\"color\":\"orange\"},{\"name\":\"4\",\"color\":\"black\"},{\"name\":\"4\",\"color\":\"blue\"},{\"name\":\"5\",\"color\":\"red\"},{\"name\":\"5\",\"color\":\"orange\"},{\"name\":\"5\",\"color\":\"black\"},{\"name\":\"5\",\"color\":\"blue\"},{\"name\":\"6\",\"color\":\"red\"},{\"name\":\"6\",\"color\":\"orange\"},{\"name\":\"6\",\"color\":\"black\"},{\"name\":\"6\",\"color\":\"blue\"},{\"name\":\"7\",\"color\":\"red\"},{\"name\":\"7\",\"color\":\"orange\"},{\"name\":\"7\",\"color\":\"black\"},{\"name\":\"7\",\"color\":\"blue\"},{\"name\":\"8\",\"color\":\"red\"},{\"name\":\"8\",\"color\":\"orange\"},{\"name\":\"8\",\"color\":\"black\"},{\"name\":\"8\",\"color\":\"blue\"},{\"name\":\"9\",\"color\":\"red\"},{\"name\":\"9\",\"color\":\"orange\"},{\"name\":\"9\",\"color\":\"black\"},{\"name\":\"9\",\"color\":\"blue\"},{\"name\":\"10\",\"color\":\"red\"},{\"name\":\"10\",\"color\":\"orange\"},{\"name\":\"10\",\"color\":\"black\"},{\"name\":\"10\",\"color\":\"blue\"},{\"name\":\"11\",\"color\":\"red\"},{\"name\":\"11\",\"color\":\"orange\"},{\"name\":\"11\",\"color\":\"black\"},{\"name\":\"11\",\"color\":\"blue\"},{\"name\":\"12\",\"color\":\"red\"},{\"name\":\"12\",\"color\":\"orange\"},{\"name\":\"12\",\"color\":\"black\"},{\"name\":\"12\",\"color\":\"blue\"},{\"name\":\"13\",\"color\":\"red\"},{\"name\":\"13\",\"color\":\"orange\"},{\"name\":\"13\",\"color\":\"black\"},{\"name\":\"13\",\"color\":\"blue\"},{\"name\":\"joker\",\"color\":\"black\"},{\"name\":\"joker\",\"color\":\"red\"},{\"name\":\"1\",\"color\":\"red\"},{\"name\":\"1\",\"color\":\"orange\"},{\"name\":\"1\",\"color\":\"black\"},{\"name\":\"1\",\"color\":\"blue\"},{\"name\":\"2\",\"color\":\"red\"},{\"name\":\"2\",\"color\":\"orange\"},{\"name\":\"2\",\"color\":\"black\"},{\"name\":\"2\",\"color\":\"blue\"},{\"name\":\"3\",\"color\":\"red\"},{\"name\":\"3\",\"color\":\"orange\"},{\"name\":\"3\",\"color\":\"black\"},{\"name\":\"3\",\"color\":\"blue\"},{\"name\":\"4\",\"color\":\"red\"},{\"name\":\"4\",\"color\":\"orange\"},{\"name\":\"4\",\"color\":\"black\"},{\"name\":\"4\",\"color\":\"blue\"},{\"name\":\"5\",\"color\":\"red\"},{\"name\":\"5\",\"color\":\"orange\"},{\"name\":\"5\",\"color\":\"black\"},{\"name\":\"5\",\"color\":\"blue\"},{\"name\":\"6\",\"color\":\"red\"},{\"name\":\"6\",\"color\":\"orange\"},{\"name\":\"6\",\"color\":\"black\"},{\"name\":\"6\",\"color\":\"blue\"},{\"name\":\"7\",\"color\":\"red\"},{\"name\":\"7\",\"color\":\"orange\"},{\"name\":\"7\",\"color\":\"black\"},{\"name\":\"7\",\"color\":\"blue\"},{\"name\":\"8\",\"color\":\"red\"},{\"name\":\"8\",\"color\":\"orange\"},{\"name\":\"8\",\"color\":\"black\"},{\"name\":\"8\",\"color\":\"blue\"},{\"name\":\"9\",\"color\":\"red\"},{\"name\":\"9\",\"color\":\"orange\"},{\"name\":\"9\",\"color\":\"black\"},{\"name\":\"9\",\"color\":\"blue\"},{\"name\":\"10\",\"color\":\"red\"},{\"name\":\"10\",\"color\":\"orange\"},{\"name\":\"10\",\"color\":\"black\"},{\"name\":\"10\",\"color\":\"blue\"},{\"name\":\"11\",\"color\":\"red\"},{\"name\":\"11\",\"color\":\"orange\"},{\"name\":\"11\",\"color\":\"black\"},{\"name\":\"11\",\"color\":\"blue\"},{\"name\":\"12\",\"color\":\"red\"},{\"name\":\"12\",\"color\":\"orange\"},{\"name\":\"12\",\"color\":\"black\"},{\"name\":\"12\",\"color\":\"blue\"},{\"name\":\"13\",\"color\":\"red\"},{\"name\":\"13\",\"color\":\"orange\"},{\"name\":\"13\",\"color\":\"black\"},{\"name\":\"13\",\"color\":\"blue\"},{\"name\":\"joker\",\"color\":\"black\"},{\"name\":\"joker\",\"color\":\"red\"}]');")
    }


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


fun mainPageGet(request: Request, response: Response): String {
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


fun xD() {

}