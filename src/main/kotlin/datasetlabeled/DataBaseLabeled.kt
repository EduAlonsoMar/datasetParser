package datasetlabeled

import javafx.collections.ObservableList
import org.apache.commons.csv.CSVParser
import tornadofx.observableListOf
import util.DateTimeUtils
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.SQLIntegrityConstraintViolationException
import java.sql.Statement

class DataBaseLabeled {
    private var insertDatasetDescription = "INSERT INTO dataset (name, description) VALUES (\"%s\", \"%s\")"
    private var insertUserIterationTemplate = "INSERT INTO user_reaction (date, userId, label, datasetId) VALUES (\"%s\", %s, \"%s\", %s)"

    // TODO: Continuar con las templates y leer el fichero obamaAnonimized para ir añadiendo cada usuario con su timestamp y su labe
    // Luego se usará SQL para ver el total de usuarios distintos y sacar a cada hora una 'foto' de como está el porcentaje de creyente y negadores

    private var connection: Connection? = null

    fun initializeConnection() {
        val jdbcUrl = "jdbc:mysql://localhost:3306/DataSetLabeled"
        if (connection == null) {
            connection = DriverManager.getConnection(jdbcUrl, "fakenews", "fakenews")
        }
        println(connection!!.isValid(0))
    }

    fun insertDataSetDescription (name: String, desc: String) {
        val query = String.format(insertDatasetDescription, name, desc)

        println(query)

        val stmt = connection?.createStatement()



        stmt?.let { statement ->
            try {
                statement.execute("DELETE FROM dataset")
                if (!statement.execute(query)) {
                    println("statement with query $query failed")
                }
            } catch (e: SQLIntegrityConstraintViolationException) {
                println("dataset $name already inserted")
            }
        }
    }

    fun getDataSetList(): ObservableList<String> {
        val query = "SELECT name FROM dataset"
        initializeConnection()
        val stmt = connection?.createStatement()
        val result = observableListOf<String>()
        stmt?.let {statement ->
            try {
                val rs = statement.executeQuery(query)
                while(rs.next()) {
                    result.add(rs.getString("name"))
                }
            } catch (e: SQLException) {
                println("Error while executing $query")
            }
        }

        return result
    }

    fun getDataSetId (name: String): Int {
        val query = String.format("SELECT id FROM dataset WHERE name=\"%s\"",name)

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

    fun insertDatasetTimedRecords(dataSetName: String, csvTimedParser: CSVParser) {
        val id = getDataSetId(dataSetName)

        val stmt: Statement? = connection?.createStatement()

        var dateTimeSanitized : String
        for (csvRecord in csvTimedParser) {
            dateTimeSanitized = csvRecord.get("date")
            val query = String.format(insertUserIterationTemplate, dateTimeSanitized, csvRecord.get("userID"), csvRecord.get("label"), id.toString())
            println(query)
            stmt?.let { statement ->
                if (!statement.execute(query)) {
                    println("\t\t Failed!!")
                }
            }
        }
    }

    fun closeConnection() {
        connection?.close()
    }
}