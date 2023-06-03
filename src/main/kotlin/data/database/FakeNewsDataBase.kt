package data.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

class FakeNewsDataBase() {

    private var connection: Connection? = null
    private var statement: Statement? = null

    fun createStatement(): Statement {
        if (connection == null) {
            connection = DriverManager.getConnection(dataBaseUrl, user, pass)
        }
        while (statement == null) {
            statement = connection?.createStatement()
        }
        return statement!!
    }

    fun closeConnection() {
        connection?.close()
        connection = null
    }

    fun logQueryError(query: String, e: SQLException) {
        print("\u001b[31m \t\t The following query Failed!! \u001b[0m")
        print(" \"${query}\"")
        println(" because of and SQLException: ${e.message}")
    }

    companion object {

        private const val dataBaseUrl = "jdbc:mysql://192.168.1.179:3306/OSNModeling"
        private const val user = "fakenews"
        private const val pass = "fakenews"

    }
}