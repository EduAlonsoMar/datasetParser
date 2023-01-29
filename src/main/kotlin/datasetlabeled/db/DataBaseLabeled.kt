package datasetlabeled.db

import javafx.collections.ObservableList
import org.apache.commons.csv.CSVParser
import tornadofx.observableListOf
import util.DateTimeUtils
import java.sql.Connection
import java.sql.Date
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.SQLIntegrityConstraintViolationException
import java.sql.Statement

open class DataBaseLabeled {

    protected var connection: Connection? = null

    protected fun initializeConnection() {
        val jdbcUrl = "jdbc:mysql://localhost:3306/DataSetLabeled"
        if (connection == null) {
            connection = DriverManager.getConnection(jdbcUrl, "fakenews", "fakenews")
        }
        println(connection!!.isValid(0))
    }





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




    fun closeConnection() {
        connection?.close()
        connection = null
    }
}