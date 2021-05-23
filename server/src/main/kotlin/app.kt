import spark.Request
import spark.Response
import spark.Spark
import spark.Spark.get

fun main() {
    println("Hello world");

    // ---------------
    // server config
    // ---------------
    Spark.port(5000)
    Spark.staticFileLocation("/public")

    Spark.before({ request, response ->
        response.header("Access-Control-Allow-Methods", "*")
        response.header("Access-Control-Allow-Origin", "*")
    })

    get("") { request, response -> mainPageGet(request, response) }

}


fun mainPageGet(request:Request, response:Response):String{
    response.type("application/json");
    return "{'a':1}";
}