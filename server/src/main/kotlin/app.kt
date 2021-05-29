import spark.Request
import spark.Response
import spark.Spark
import spark.Spark.get
import spark.kotlin.Http
import spark.kotlin.ignite
import java.sql.DriverManager


fun main() {
    println("Hello world");
    val conn = DriverManager.getConnection("jdbc:h2:mem:~/base1;DB_CLOSE_DELAY=-1")
    try {
        val stmt = conn.createStatement()
        stmt.execute("CREATE TABLE TBL01(id int primary key auto_increment, imie varchar(255) not null);")
        conn.close()
    } catch (e: Exception) {
        println(e.message)
    }

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

        Spark.before({ request, response ->
            response.header("Access-Control-Allow-Methods", "*")
            response.header("Access-Control-Allow-Origin", "*")
        })
        get("/") { request, response -> mainPageGet(request, response) }
    }
}


fun mainPageGet(request:Request, response:Response):String{
    response.type("application/json");
    println("ee")
    val conn = DriverManager.getConnection("jdbc:h2:mem:~/base1;DB_CLOSE_DELAY=-1")
    val stmt2 = conn.createStatement()
    val rs2 = stmt2.execute("INSERT INTO TBL01 (imie) VALUES ('rre');")
    val stmt = conn.createStatement()
    val rs = stmt.executeQuery("SELECT id, imie FROM TBL01;")
    while (rs.next()) {
                println("wiersz ${rs.getString("id")}  " +
                        "${rs.getString("imie")}")
    }
    conn.close()
    sqlQuery.insert()
    return "{'a':1}";
}

fun getHerokuPort(): Int {
    val processBuilder = ProcessBuilder()
    return if (processBuilder.environment()["PORT"] != null) {
        processBuilder.environment()["PORT"]!!.toInt()
    } else 5000
}