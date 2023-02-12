package database.databaselabeled.db

import org.apache.commons.csv.CSVParser
import java.sql.SQLException
import java.sql.SQLIntegrityConstraintViolationException
import java.sql.Statement

class DataBaseInsertions: DataBaseLabeled() {


    fun insertDataSetDescription (name: String, desc: String) {
        initializeConnection()
        val query = String.format(insertDatasetDescription, name, desc)

        val stmt = connection?.createStatement()

        stmt?.let { statement ->
            try {
                statement.execute(query)
            } catch (e: SQLIntegrityConstraintViolationException) {
                logQueryError(query, e)
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
                    logQueryError(queryForUsers, e)
                }
                try {
                    statement.execute(queryForOpinions)
                } catch (e: SQLException){
                    logQueryError(queryForOpinions, e)
                }

            }
        }
    }

    fun destroy() {
        closeConnection()
    }

    companion object {
        private const val insertDatasetDescription = "INSERT INTO dataset (name, description) VALUES (\"%s\", \"%s\")"
        private const val insertUserTemplate = "INSERT INTO user (id) VALUES (\"%s\")"
        private const val insertOpinionTemplate ="INSERT INTO opinion (datasetId, userId, date, label) VALUES (%s, %s, \"%s\", \"%s\")"
    }

}