package data.datasource

import data.database.FakeNewsDataBase
import data.database.model.DatasetLabeled
import data.database.model.Opinion
import data.database.model.UserLabeled
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.Date
import java.sql.SQLException
import java.sql.SQLIntegrityConstraintViolationException
import java.sql.Statement
import java.util.*

open class DatasetLabeledDataSource : KoinComponent {


    private val db: FakeNewsDataBase by inject()

    fun getDataSetId(name: String): Int {
        val query = String.format(getDataSetIdTemplate, name)
        val statement = db.createStatement()
        try {
            val rs = statement.executeQuery(query)
            while (rs.next()) {
                return rs.getInt(idColumnName)
            }
        } catch (e: SQLIntegrityConstraintViolationException) {
            println("dataset $name already inserted")
        }
        throw RuntimeException("No dataset with that name")
    }

    fun getListOfDataSets(): List<DatasetLabeled> {
        val result = mutableListOf<DatasetLabeled>()
        val statement = db.createStatement()
        try {
            val queryResult = statement.executeQuery(getListOfDataSets)
            while (queryResult.next()) {
                result.add(
                    DatasetLabeled(
                        id = queryResult.getInt(dataSetLabeledIdColumnName),
                        name = queryResult.getString(dataSetLabeledNameColumn),
                        description = queryResult.getString(dataSetLabeledDescriptionColumn)
                    )
                )
            }
        } catch (e: SQLException) {
            db.logQueryError(getListOfDataSets, e)
        }


        return result
    }

    fun insertNewDataset(name: String, desc: String?): Int {
        val descriptionToUse = desc ?: ""
        val query = String.format(insertDatasetDescription, name, descriptionToUse)
        val queryForTheId = String.format(getDataSetIdTemplate, name)
        val statement = db.createStatement()
        try {
            statement.execute(query)
            val result = statement.executeQuery(queryForTheId)
            while (result.next()) {
                return result.getInt(idColumnName)
            }
        } catch (e: SQLIntegrityConstraintViolationException) {
            db.logQueryError(query, e)

        }

        return -1
    }

    fun insertDatasetTimedRecords(user: UserLabeled, opinion: Opinion) {
        val statement = db.createStatement()
        val queryForUsers: String = String.format(insertUserTemplate, user.id)
        val queryForOpinions: String = String.format(
            insertOpinionTemplate,
            opinion.dataSetId.toString(),
            opinion.userId,
            opinion.date,
            opinion.label
        )

        try {
            statement.execute(queryForUsers)
        } catch (e: SQLException) {
            db.logQueryError(queryForUsers, e)
        }

        try {
            statement.execute(queryForOpinions)
        } catch (e: SQLException) {
            db.logQueryError(queryForOpinions, e)
        }

    }

    fun getNumberOfDistinctUsers(datasetId: Int): Int {
        val stmt: Statement = db.createStatement()
        val query = String.format(
            numberOfDistinctUsersQuery,
            datasetId
        )
        try {
            val res = stmt.executeQuery(query)
            if (res.next()) {
                return res.getInt("users")
            }
        } catch (e: SQLException) {
            db.logQueryError(query, e)
        }

        return 0
    }

    fun getEarliestDate(datasetId: Int): Date? {
        val query = String.format(
            earliestDateTemplate,
            datasetId
        )
        val statement = db.createStatement()

        try {
            val res = statement.executeQuery(query)
            if (res.next()) {
                return res.getDate("earliestDate")
            }
        } catch (e: SQLException) {
            db.logQueryError(query, e)
        }

        return null
    }

    fun getOldestDate(datasetName: String): Date? {
        val query = String.format(
            oldestDateTemplate,
            getDataSetId(datasetName)
        )

        val statement = db.createStatement()

        try {
            val res = statement.executeQuery(query)
            if (res.next()) {
                return res.getDate("oldestDate")
            }
        } catch (e: SQLException) {
            db.logQueryError(query, e)
        }

        return null
    }

    fun getDataSetList(): ArrayList<DatasetLabeled> {
        val statement = db.createStatement()
        val result = arrayListOf<DatasetLabeled>()

        try {
            val rs = statement.executeQuery(getNamesListQuery)
            while (rs.next()) {
                result.add(
                    DatasetLabeled(
                        rs.getInt(dataSetLabeledIdColumnName),
                        rs.getString(dataSetLabeledNameColumn),
                        rs.getString(dataSetLabeledDescriptionColumn)
                    )
                )
            }
        } catch (e: SQLException) {
            db.logQueryError(getNamesListQuery, e)
        }

        return result
    }

    private fun getNumberOfUsersInDayDependingOnLabel(day: Calendar, label: String): Int? {
        val query = String.format(
            getNumberOfUsersPerDayAndLabelTemplate,
            day.get(Calendar.YEAR),
            day.get(Calendar.MONTH) + 1,
            day.get(Calendar.DAY_OF_MONTH),
            label
        )

        val statement = db.createStatement()

        try {
            val rs = statement.executeQuery(query)
            while (rs.next()) {
                return rs.getInt("users")
            }
        } catch (e: SQLException) {
            db.logQueryError(query, e)
        }


        return null
    }

    fun getUsersInDay(day: Calendar, datasetId: Int): Map<Int, String> {
        val query = String.format(
            getUserInDayTemplate,
            day.get(Calendar.YEAR),
            day.get(Calendar.MONTH) + 1,
            day.get(Calendar.DAY_OF_MONTH),
            datasetId
        )
        val result = mutableMapOf<Int, String>()
        val statement = db.createStatement()
        try {
            val rs = statement.executeQuery(query)
            while (rs.next()) {
                if (result.containsKey(rs.getInt("userId"))) {
                    result.replace(rs.getInt("userId"), rs.getString("label"))
                } else {
                    result[rs.getInt("userId")] = rs.getString("label")
                }
            }
        } catch (e: SQLException) {
            db.logQueryError(query, e)
        }

        return result
    }


    companion object {
        const val idColumnName = "idDataSetLabeled"
        const val getDataSetIdTemplate = "SELECT $idColumnName FROM datasetlabeled WHERE name=\"%s\""

        private const val dataSetLabeledTableName = "DataSetLabeled"
        const val dataSetLabeledIdColumnName = "idDataSetLabeled"
        const val dataSetLabeledNameColumn = "name"
        const val dataSetLabeledDescriptionColumn = "description"

        private const val userLabeledTableName = "UserLabeled"
        private const val userLabeledIdName = "idUserLabeled"
        private const val userLabeledLabelColumn = "label"
        private const val userDataSetIdColumn = "datasetId"

        private const val opinionTableName = "opinion"
        private const val opinionIdName = "idOpinion"
        private const val opinionDatasetIdName = "datasetId"
        private const val opinionUserIdName = "userId"
        private const val opinionDateName = "date"
        private const val opinionLabel = "label"


        const val insertDatasetDescription =
            "INSERT INTO $dataSetLabeledTableName ($dataSetLabeledNameColumn, $dataSetLabeledDescriptionColumn) VALUES (\"%s\", \"%s\")"


        const val insertUserTemplate = "INSERT INTO $userLabeledTableName ($userLabeledIdName) VALUES (%s)"


        const val insertOpinionTemplate =
            "INSERT INTO $opinionTableName ($opinionDatasetIdName, $opinionUserIdName, $opinionDateName, $opinionLabel) VALUES (%s, %s, \"%s\", \"%s\")"

        const val createDataSetLabeled = "CREATE TABLE $dataSetLabeledTableName ( " +
                "$dataSetLabeledIdColumnName INT NOT NULL AUTO_INCREMENT, " +
                "$dataSetLabeledNameColumn VARCHAR(128) NULL, " +
                "$dataSetLabeledDescriptionColumn VARCHAR(256) NULL, " +
                "PRIMARY KEY ($dataSetLabeledIdColumnName))"

        const val createUserLabeled = "CREATE TABLE $userLabeledTableName ( " +
                "$userLabeledIdName INT NOT NULL, " +
                "PRIMARY KEY ($userLabeledTableName))"

        const val creaeteOpinionLabeled = "CREATE TABLE $opinionTableName ( " +
                "$opinionIdName INT NOT NULL AUTO_INCREMENT, " +
                "$opinionDatasetIdName INT NOT NULL, " +
                "$opinionUserIdName INT NOT NULL, " +
                "$opinionDateName DATETIME NOT NULL, " +
                "$opinionLabel VARCHAR(45) NULL, " +
                "PRIMARY KEY ($opinionIdName), " +
                "INDEX userLabeledId_idx ($opinionUserIdName ASC) VISIBLE, " +
                "INDEX datasetLabeledId_idx ($opinionDateName ASC) VISIBLE, " +
                "CONSTRAINT datasetLabeledId " +
                " FOREIGN KEY (datasetId) " +
                " REFERENCES $dataSetLabeledTableName ($dataSetLabeledIdColumnName) " +
                "ON DELETE NO ACTION " +
                "ON DELETE NO ACTION, " +
                " CONSTRAINT userLabeledId " +
                "FOREIGN KEY (userId) " +
                "REFERENCES $userLabeledTableName ($userLabeledIdName) " +
                "ON DELETE NO ACTION " +
                "ON DELETE NO ACTION)"

        const val numberOfDistinctUsersQuery =
            "SELECT distinct count(distinct($opinionUserIdName)) as users FROM $opinionTableName WHERE $opinionDatasetIdName = %d"
        const val earliestDateTemplate =
            "SELECT Min($opinionDateName) as earliestDate FROM $opinionTableName WHERE $opinionDatasetIdName = %d"
        const val oldestDateTemplate =
            "SELECT Max($opinionDateName) as oldestDate FROM $opinionTableName WHERE $opinionDatasetIdName = %s"
        const val getNamesListQuery = "SELECT * FROM $dataSetLabeledTableName"
        const val getNumberOfUsersPerDayAndLabelTemplate =
            "SELECT distinct count(distinct($opinionUserIdName)) as users FROM $opinionTableName WHERE $opinionDateName <= \"%s-%s-%s\" and $opinionLabel = \"%s\""
        const val getUserInDayTemplate =
            "SELECT $opinionUserIdName, $opinionLabel FROM $opinionTableName WHERE $opinionDateName <= \"%s-%s-%s\" and $opinionDatasetIdName = %s ORDER BY $opinionDateName"
        const val getListOfDataSets = "SELECT * FROM $dataSetLabeledTableName"
    }
}