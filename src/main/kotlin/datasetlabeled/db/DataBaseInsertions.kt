package datasetlabeled.db

import org.apache.commons.csv.CSVParser
import java.sql.SQLException
import java.sql.SQLIntegrityConstraintViolationException
import java.sql.Statement

class DataBaseInsertions: DataBaseLabeled() {
    private var insertDatasetDescription = "INSERT INTO dataset (name, description) VALUES (\"%s\", \"%s\")"
    private var insertUserTemplate = "INSERT INTO user (id) VALUES (\"%s\")"
    private var insertOpinionTemplate ="INSERT INTO opinion (datasetId, userId, date, label) VALUES (%s, %s, \"%s\", \"%s\")"

    fun insertDataSetDescription (name: String, desc: String) {
        initializeConnection()
        val query = String.format(insertDatasetDescription, name, desc)

        println(query)

        val stmt = connection?.createStatement()



        stmt?.let { statement ->
            try {

                if (!statement.execute(query)) {
                    println("statement with query $query failed")
                }
            } catch (e: SQLIntegrityConstraintViolationException) {
                println("dataset $name already inserted")
            }
        }
    }

    fun insertDatasetTimedRecords(dataSetName: String, csvTimedParser: CSVParser) {
        val id = getDataSetId(dataSetName)

        val stmt: Statement? = connection?.createStatement()

        var queryForUsers: String
        var queryForOpinions: String

        var dateTimeSanitized : String
        for (csvRecord in csvTimedParser) {
            dateTimeSanitized = csvRecord.get("date")
            queryForUsers = String.format(insertUserTemplate, csvRecord.get("userID"), csvRecord.get("label"), id.toString())
            queryForOpinions = String.format(insertOpinionTemplate, id.toString(), csvRecord.get("userID"), csvRecord.get("date"), csvRecord.get("label"))
            stmt?.let { statement ->
                try {
                    statement.execute(queryForUsers)
                } catch (e: SQLException) {
                    print("\u001b[31m \t\t The following query Failed!! \u001b[0m")
                    print(" \"${queryForUsers}\"")
                    println(" because of and SQLException: ${e.message}")
                }
                try {
                    statement.execute(queryForOpinions)
                } catch (e: SQLException){
                    print("\u001b[31m \t\t The following query Failed!! \u001b[0m")
                    print(" \"${queryForOpinions}\"")
                    println(" because of and SQLException: ${e.message}")
                }

            }
        }
    }

    fun destroy() {
        closeConnection()
    }

}