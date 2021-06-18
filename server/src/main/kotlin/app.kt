package main.kotlin

import com.google.gson.Gson
import main.kotlin.methods.WebSocket.WebSocketObject
import main.kotlin.methods.roomManage.roomData.cardCheck
import main.kotlin.methods.roomManage.roomData.pointCheck
import main.kotlin.methods.roomManage.roomData.randomCards
import main.kotlin.methods.sqlQuery
import spark.Request
import spark.Response
import spark.Spark
import spark.Spark.get
import spark.Spark.post
import spark.Spark.webSocket
import spark.kotlin.Http
import spark.kotlin.ignite

fun main() {

    println("Hello world");


    sqlQuery.execute(
        "CREATE TABLE IF NOT EXISTS Constants(" +
                "id int primary key auto_increment, " +
                "key varchar(255) not null," +
                "value text not null"+
                ");"
    )

    val valueTable = sqlQuery.select("SELECT * FROM Constants")

    if(valueTable.size == 0){
        sqlQuery.execute("INSERT INTO Constants (key, value) VALUES ('cards', '[{\"ID\":0,\"name\":\"1\",\"color\":\"red\"},{\"ID\":1,\"name\":\"1\",\"color\":\"orange\"},{\"ID\":2,\"name\":\"1\",\"color\":\"black\"},{\"ID\":3,\"name\":\"1\",\"color\":\"blue\"},{\"ID\":4,\"name\":\"2\",\"color\":\"red\"},{\"ID\":5,\"name\":\"2\",\"color\":\"orange\"},{\"ID\":6,\"name\":\"2\",\"color\":\"black\"},{\"ID\":7,\"name\":\"2\",\"color\":\"blue\"},{\"ID\":8,\"name\":\"3\",\"color\":\"red\"},{\"ID\":9,\"name\":\"3\",\"color\":\"orange\"},{\"ID\":10,\"name\":\"3\",\"color\":\"black\"},{\"ID\":11,\"name\":\"3\",\"color\":\"blue\"},{\"ID\":12,\"name\":\"4\",\"color\":\"red\"},{\"ID\":13,\"name\":\"4\",\"color\":\"orange\"},{\"ID\":14,\"name\":\"4\",\"color\":\"black\"},{\"ID\":15,\"name\":\"4\",\"color\":\"blue\"},{\"ID\":16,\"name\":\"5\",\"color\":\"red\"},{\"ID\":17,\"name\":\"5\",\"color\":\"orange\"},{\"ID\":18,\"name\":\"5\",\"color\":\"black\"},{\"ID\":19,\"name\":\"5\",\"color\":\"blue\"},{\"ID\":20,\"name\":\"6\",\"color\":\"red\"},{\"ID\":21,\"name\":\"6\",\"color\":\"orange\"},{\"ID\":22,\"name\":\"6\",\"color\":\"black\"},{\"ID\":23,\"name\":\"6\",\"color\":\"blue\"},{\"ID\":24,\"name\":\"7\",\"color\":\"red\"},{\"ID\":25,\"name\":\"7\",\"color\":\"orange\"},{\"ID\":26,\"name\":\"7\",\"color\":\"black\"},{\"ID\":27,\"name\":\"7\",\"color\":\"blue\"},{\"ID\":28,\"name\":\"8\",\"color\":\"red\"},{\"ID\":29,\"name\":\"8\",\"color\":\"orange\"},{\"ID\":30,\"name\":\"8\",\"color\":\"black\"},{\"ID\":31,\"name\":\"8\",\"color\":\"blue\"},{\"ID\":32,\"name\":\"9\",\"color\":\"red\"},{\"ID\":33,\"name\":\"9\",\"color\":\"orange\"},{\"ID\":34,\"name\":\"9\",\"color\":\"black\"},{\"ID\":35,\"name\":\"9\",\"color\":\"blue\"},{\"ID\":36,\"name\":\"10\",\"color\":\"red\"},{\"ID\":37,\"name\":\"10\",\"color\":\"orange\"},{\"ID\":38,\"name\":\"10\",\"color\":\"black\"},{\"ID\":39,\"name\":\"10\",\"color\":\"blue\"},{\"ID\":40,\"name\":\"11\",\"color\":\"red\"},{\"ID\":41,\"name\":\"11\",\"color\":\"orange\"},{\"ID\":42,\"name\":\"11\",\"color\":\"black\"},{\"ID\":43,\"name\":\"11\",\"color\":\"blue\"},{\"ID\":44,\"name\":\"12\",\"color\":\"red\"},{\"ID\":45,\"name\":\"12\",\"color\":\"orange\"},{\"ID\":46,\"name\":\"12\",\"color\":\"black\"},{\"ID\":47,\"name\":\"12\",\"color\":\"blue\"},{\"ID\":48,\"name\":\"13\",\"color\":\"red\"},{\"ID\":49,\"name\":\"13\",\"color\":\"orange\"},{\"ID\":50,\"name\":\"13\",\"color\":\"black\"},{\"ID\":51,\"name\":\"13\",\"color\":\"blue\"},{\"ID\":52,\"name\":\"joker\",\"color\":\"black\"},{\"ID\":53,\"name\":\"joker\",\"color\":\"red\"},{\"ID\":54,\"name\":\"1\",\"color\":\"red\"},{\"ID\":55,\"name\":\"1\",\"color\":\"orange\"},{\"ID\":56,\"name\":\"1\",\"color\":\"black\"},{\"ID\":57,\"name\":\"1\",\"color\":\"blue\"},{\"ID\":58,\"name\":\"2\",\"color\":\"red\"},{\"ID\":59,\"name\":\"2\",\"color\":\"orange\"},{\"ID\":60,\"name\":\"2\",\"color\":\"black\"},{\"ID\":61,\"name\":\"2\",\"color\":\"blue\"},{\"ID\":62,\"name\":\"3\",\"color\":\"red\"},{\"ID\":63,\"name\":\"3\",\"color\":\"orange\"},{\"ID\":64,\"name\":\"3\",\"color\":\"black\"},{\"ID\":65,\"name\":\"3\",\"color\":\"blue\"},{\"ID\":66,\"name\":\"4\",\"color\":\"red\"},{\"ID\":67,\"name\":\"4\",\"color\":\"orange\"},{\"ID\":68,\"name\":\"4\",\"color\":\"black\"},{\"ID\":69,\"name\":\"4\",\"color\":\"blue\"},{\"ID\":70,\"name\":\"5\",\"color\":\"red\"},{\"ID\":71,\"name\":\"5\",\"color\":\"orange\"},{\"ID\":72,\"name\":\"5\",\"color\":\"black\"},{\"ID\":73,\"name\":\"5\",\"color\":\"blue\"},{\"ID\":74,\"name\":\"6\",\"color\":\"red\"},{\"ID\":75,\"name\":\"6\",\"color\":\"orange\"},{\"ID\":76,\"name\":\"6\",\"color\":\"black\"},{\"ID\":77,\"name\":\"6\",\"color\":\"blue\"},{\"ID\":78,\"name\":\"7\",\"color\":\"red\"},{\"ID\":79,\"name\":\"7\",\"color\":\"orange\"},{\"ID\":80,\"name\":\"7\",\"color\":\"black\"},{\"ID\":81,\"name\":\"7\",\"color\":\"blue\"},{\"ID\":82,\"name\":\"8\",\"color\":\"red\"},{\"ID\":83,\"name\":\"8\",\"color\":\"orange\"},{\"ID\":84,\"name\":\"8\",\"color\":\"black\"},{\"ID\":85,\"name\":\"8\",\"color\":\"blue\"},{\"ID\":86,\"name\":\"9\",\"color\":\"red\"},{\"ID\":87,\"name\":\"9\",\"color\":\"orange\"},{\"ID\":88,\"name\":\"9\",\"color\":\"black\"},{\"ID\":89,\"name\":\"9\",\"color\":\"blue\"},{\"ID\":90,\"name\":\"10\",\"color\":\"red\"},{\"ID\":91,\"name\":\"10\",\"color\":\"orange\"},{\"ID\":92,\"name\":\"10\",\"color\":\"black\"},{\"ID\":93,\"name\":\"10\",\"color\":\"blue\"},{\"ID\":94,\"name\":\"11\",\"color\":\"red\"},{\"ID\":95,\"name\":\"11\",\"color\":\"orange\"},{\"ID\":96,\"name\":\"11\",\"color\":\"black\"},{\"ID\":97,\"name\":\"11\",\"color\":\"blue\"},{\"ID\":98,\"name\":\"12\",\"color\":\"red\"},{\"ID\":99,\"name\":\"12\",\"color\":\"orange\"},{\"ID\":100,\"name\":\"12\",\"color\":\"black\"},{\"ID\":101,\"name\":\"12\",\"color\":\"blue\"},{\"ID\":102,\"name\":\"13\",\"color\":\"red\"},{\"ID\":103,\"name\":\"13\",\"color\":\"orange\"},{\"ID\":104,\"name\":\"13\",\"color\":\"black\"},{\"ID\":105,\"name\":\"13\",\"color\":\"blue\"},{\"ID\":106,\"name\":\"joker\",\"color\":\"black\"},{\"ID\":107,\"name\":\"joker\",\"color\":\"red\"}]');")
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

        get("/game"){request,response -> response.redirect("/game/index.html")}


        get("/getRoomConfig"){ request, response -> getRoomConfig(request, response) }
        post("/setConfig"){ request, response -> setConfig(request, response) }
    }
}


fun mainPageGet(request: Request, response: Response): String {
    response.type("application/json");

    return "{'a':1}";
}
fun getRoomConfig(request: Request, response: Response): String {
    return Gson().toJson(mapOf(
        "randomCards" to randomCards,
        "cardCheck" to cardCheck,
        "pointCheck" to pointCheck,
    ))
}

fun setConfig(request: Request, response: Response):String {
    val parsedData = Gson().fromJson(request.body(), MutableMap::class.java)

    if(parsedData["randomCards"] != null){
        randomCards = parsedData["randomCards"] as Boolean
    }

    if(parsedData["cardCheck"] != null){
        cardCheck = parsedData["cardCheck"] as Boolean
    }

    if(parsedData["pointCheck"] != null){
        pointCheck = parsedData["pointCheck"] as Boolean
    }

    return "{\"Passed\": true}"
}


fun getHerokuPort(): Int {
    val processBuilder = ProcessBuilder()
    return if (processBuilder.environment()["PORT"] != null) {
        processBuilder.environment()["PORT"]!!.toInt()
    } else 5000
}