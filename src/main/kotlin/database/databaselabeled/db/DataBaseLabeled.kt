package database.databaselabeled.db

import database.FakeNewsDataBase
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLIntegrityConstraintViolationException

open class DataBaseLabeled: FakeNewsDataBase(dataBaseUrl, user, pass) {


    protected fun getDataSetId (name: String): Int {
        val query = String.format("SELECT id FROM dataset WHERE name=\"%s\"",name)
        initializeConnection()
        val stmt = connection?.createStatement()

        stmt?.let { statement ->
            try {
                val rs = statement.executeQuery(query)
                while(rs.next()) {
                    return rs.getInt("id")
                }
            } catch (e: SQLIntegrityConstraintViolationException) {
                println("dataset $name already inserted")
            }
        }

        throw RuntimeException("No dataset with that name")
    }




    companion object {
        private const val dataBaseUrl = "jdbc:mysql://localhost:3306/DataSetLabeled"
        private const val user = "fakenews"
        private const val pass = "fakenews"
    }
}