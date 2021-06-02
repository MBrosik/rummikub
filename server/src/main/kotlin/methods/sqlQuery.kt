package methods

import java.sql.DriverManager


object sqlQuery {
    private val conn_name = "jdbc:h2:mem:~/base1;DB_CLOSE_DELAY=-1"

    fun execute(sql:String){
        val conn = DriverManager.getConnection(conn_name)
        val stmt = conn.createStatement()
        stmt.execute(sql)
        conn.close()
    }
    fun select(sql:String): ArrayList<HashMap<Any, Any>> {
        // ---------------
        // get connection
        // ---------------
        val conn = DriverManager.getConnection(conn_name)

        // -----------------
        // create statement
        // -----------------
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery(sql)

        // ---------------
        // return list
        // ---------------
        val dataList = ArrayList<HashMap<Any, Any>>();

        // ---------------
        // fetch data
        // ---------------
        while (rs.next()) {
            val Hashmap = hashMapOf<Any, Any>()

            for (i in 1..rs.metaData.columnCount){
                Hashmap.put(rs.metaData.getColumnName(i), rs.getObject(i))
            }
            dataList.add(Hashmap)
        }
        conn.close()

        return dataList
    }
}