package database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

open class FakeNewsDataBase(val jdbcUrl: String, val user: String, val pass: String) {

    protected var connection: Connection? = null

    protected fun initializeConnection() {
        if (connection == null) {
            connection = DriverManager.getConnection(jdbcUrl, user, pass)
        }
        // println(connection!!.isValid(0))
    }

    fun closeConnection() {
        connection?.close()
        connection = null
    }

    protected fun logQueryError(query: String, e: SQLException) {
        print("\u001b[31m \t\t The following query Failed!! \u001b[0m")
        print(" \"${query}\"")
        println(" because of and SQLException: ${e.message}")
    }
}