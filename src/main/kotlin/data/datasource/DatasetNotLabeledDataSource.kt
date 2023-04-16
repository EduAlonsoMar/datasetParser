package data.datasource

import data.database.DataBaseUtils.sanitizeText
import data.database.DataBaseUtils.sanitizeUserName
import data.database.FakeNewsDataBase
import data.database.model.DatasetNotLabeled
import data.database.model.Tuit
import data.database.model.UserNotLabeled
import data.parser.model.TuitNewsRecord
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import util.DateTimeUtils
import java.sql.SQLException
import java.sql.SQLIntegrityConstraintViolationException
import java.sql.Timestamp
import java.util.*

open class DatasetNotLabeledDataSource : KoinComponent {

    private val db: FakeNewsDataBase by inject()

    private lateinit var listNewsTuits: ArrayList<TuitNewsRecord>


    fun insertDatasetNotLabeled(dataset: DatasetNotLabeled) {
        val stmt = db.createStatement()

        val title = sanitizeText(dataset.title)
        val content = sanitizeText(dataset.content)
        val query = String.format(insertNewsTemplate, dataset.id, title, content)
        try {
            (stmt.execute(query))
        } catch (e: SQLException) {
            db.logQueryError(query, e)
        }
    }


    fun insertUser(user: UserNotLabeled) {
        val stmt = db.createStatement()
        val userName = sanitizeUserName(user.userName)
        val query = String.format(insertUserTemplate, user.id, userName, user.followers, user.friends)
        try {
            stmt.execute(query)
        } catch (e: SQLIntegrityConstraintViolationException) {
            db.logQueryError(query, e)
        }

    }

    fun insertTuit(tuit: Tuit) {
        val stmt = db.createStatement()
        val query = String.format(
            insertTuitTemplate,
            tuit.id,
            DateTimeUtils.convertTimeToDateTime(tuit.timestamp),
            sanitizeUserName(tuit.text),
            tuit.newsIndex.toString(),
            tuit.userId.toString()
        )
        try {
            stmt.execute(query)
        } catch (e: SQLException) {
            db.logQueryError(query, e)
        }

    }

    fun getFakeNewsWithMostFakeNewsUsers(): ArrayList<DatasetNotLabeled> {
        val resultList = arrayListOf<DatasetNotLabeled>()
        try {
            val stmt = db.createStatement()
            val result = stmt.executeQuery(selectMostFakeNewsUsers)
            result?.let { resultSet ->
                while (resultSet.next()) {
                    resultList.add(
                        DatasetNotLabeled(
                            resultSet.getInt(dataSetNotLabeledIdColumnName).toString(),
                            resultSet.getString(dataSetNotLabeledTitleColumn),
                            resultSet.getString(dataSetNotLabeledContentColumn)
                        )
                    )
                }
            }
        } catch (e: SQLException) {
            db.logQueryError(selectMostFakeNewsUsers, e)
        }

        return resultList
    }

    fun getDataSets(): ArrayList<DatasetNotLabeled> {
        val resultList = arrayListOf<DatasetNotLabeled>()
        try {
            val stmt = db.createStatement()
            val result = stmt.executeQuery(selectFakeNews)
            result?.let { resultSet ->
                while (resultSet.next()) {
                    resultList.add(
                        DatasetNotLabeled(
                            resultSet.getInt(dataSetNotLabeledIdColumnName).toString(),
                            resultSet.getString(dataSetNotLabeledTitleColumn),
                            resultSet.getString(dataSetNotLabeledContentColumn)
                        )
                    )
                }
            }
        } catch (e: SQLException) {
            db.logQueryError(selectMostFakeNewsUsers, e)
        }

        return resultList
    }

    fun getFakeNewsIdFromTitle(title: String): Int {
        val queryText = String.format(getFakeNewsIdBasedOnTitle, title)
        try {
            val stmt = db.createStatement()
            val result = stmt?.executeQuery(queryText)
            result?.let { resultSet ->
                if (resultSet.next()) {
                    return resultSet.getInt(dataSetNotLabeledIdColumnName)
                }
            }
        } catch (e: SQLException) {
            db.logQueryError(queryText, e)
        }

        return 0
    }

    fun getEarliestDate(fakeNewsId: Int): Timestamp? {
        val queryText = String.format(getEarliestDatForTuitInDataSet, fakeNewsId)
        try {
            val stmt = db.createStatement()
            val result = stmt.executeQuery(queryText)
            result?.let { resultSet ->
                if (resultSet.next()) {
                    return resultSet.getTimestamp("creation_date")
                }
            }
        } catch (e: SQLException) {
            db.logQueryError(queryText, e)
        }

        return null
    }

    fun getUsersInHour(c: Calendar, fakeNewsId: Int): Int {
        val c2 = Calendar.getInstance()
        c2.time = c.time
        c2.add(Calendar.HOUR_OF_DAY, 1)
        val queryText = String.format(
            getUsersInHour,
            fakeNewsId,
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH) + 1,
            c.get(Calendar.DAY_OF_MONTH),
            c.get(Calendar.HOUR_OF_DAY),
            c2.get(Calendar.YEAR),
            c2.get(Calendar.MONTH) + 1,
            c2.get(Calendar.DAY_OF_MONTH),
            c2.get(Calendar.HOUR_OF_DAY)
        )
        try {
            val stmt = db.createStatement()
            val result = stmt.executeQuery(queryText)
            result?.let { resultSet ->
                if (resultSet.next()) {
                    return resultSet.getInt("users")
                }
            }
        } catch (e: SQLException) {
            db.logQueryError(queryText, e)
        }

        return 0
    }

    fun getUsersInDay(c: Calendar, datasetId: Int): Int {
        val c2 = Calendar.getInstance()
        c2.time = c.time
        c2.add(Calendar.DAY_OF_MONTH, 1)
        val query = String.format(
            getUsersInDay,
            datasetId,
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH) + 1,
            c.get(Calendar.DAY_OF_MONTH),
            c2.get(Calendar.YEAR),
            c2.get(Calendar.MONTH) + 1,
            c2.get(Calendar.DAY_OF_MONTH)
        )
        try {
            val stmt = db.createStatement()
            val result = stmt.executeQuery(query)
            result?.let { resultSet ->
                if (resultSet.next()) {
                    return resultSet.getInt("users")
                }
            }
        } catch (e: SQLException) {
            db.logQueryError(query, e)
        }

        return 0
    }

    fun getTotalUsersForFakeNews(fakeNewsId: Int): Int {
        val queryText = String.format(getNumberOfUsersForFakeNews, fakeNewsId)
        try {
            val stmt = db.createStatement()
            val result = stmt.executeQuery(queryText)
            result?.let { resultSet ->
                if (resultSet.next()) {
                    return resultSet.getInt("users")
                }
            }
        } catch (e: SQLException) {
            db.logQueryError(queryText, e)
        }
        return 0
    }

    companion object {

        const val dataSetNotLabeledTableName = "DataSetNotLabeled"
        const val dataSetNotLabeledIdColumnName = "idDataSetNotLabeled"
        const val dataSetNotLabeledTitleColumn = "title"
        const val dataSetNotLabeledContentColumn = "content"

        const val tuitTableName = "Tuit"
        const val tuitIdColumn = "idTuit"
        const val tuitTextColumn = "text"
        const val tuitNewsIndexColumn = "news_index"
        const val tuitCreationDateColumn = "creation_date"
        const val tuitUserIdConlumn = "user_idUser"

        const val userNotLabeledTableName = "UserNotLabeled"
        const val userNotLabeledIdColumn = "idUserNotLabeled"
        const val userNotLabeledNameColumn = "userName"
        const val userNotLabeledFollowersColumn = "followers"
        const val userNotLabeledFriendsColumn = "friends"

        const val selectMostFakeNewsUsers =
            "SELECT " +
                    "$tuitNewsIndexColumn, " +
                    "$dataSetNotLabeledIdColumnName, " +
                    "$dataSetNotLabeledTitleColumn, " +
                    "$dataSetNotLabeledContentColumn, " +
                    "COUNT($tuitNewsIndexColumn) AS news_occurrence " +
                    "FROM " +
                    "$tuitTableName, " +
                    "$dataSetNotLabeledTableName " +
                    "WHERE " +
                    "$dataSetNotLabeledIdColumnName = $tuitNewsIndexColumn " +
                    "GROUP BY " +
                    "$tuitNewsIndexColumn " +
                    "ORDER BY " +
                    "news_occurrence DESC " +
                    "LIMIT 10"

        const val selectFakeNews =
            "SELECT * FROM $dataSetNotLabeledTableName"

        const val getFakeNewsIdBasedOnTitle =
            "SELECT " +
                    "$dataSetNotLabeledIdColumnName " +
                    "FROM " +
                    "$dataSetNotLabeledTableName " +
                    "WHERE " +
                    "$dataSetNotLabeledTitleColumn = \"%s\""

        const val getNumberOfUsersForFakeNews =
            "SELECT " +
                    "COUNT(distinct($tuitUserIdConlumn)) as users " +
                    "FROM " +
                    "$tuitTableName " +
                    "WHERE " +
                    "$tuitNewsIndexColumn = %d"

        const val getEarliestDatForTuitInDataSet =
            "SELECT " +
                    "$tuitCreationDateColumn " +
                    "FROM " +
                    "$tuitTableName " +
                    "WHERE " +
                    "$tuitNewsIndexColumn = %d " +
                    "ORDER BY " +
                    "$tuitCreationDateColumn " +
                    "ASC"

        const val getUsersInHour =
            "SELECT " +
                    "COUNT(distinct($tuitUserIdConlumn)) as users " +
                    "FROM " +
                    "$tuitTableName " +
                    "WHERE " +
                    "$tuitNewsIndexColumn = %d AND " +
                    "$tuitCreationDateColumn > \"%s-%s-%s %s:00:00\" AND " +
                    "$tuitCreationDateColumn < \"%s-%s-%s %s:00:00\""

        const val getUsersInDay =
            "SELECT " +
                    "COUNT(distinct($tuitUserIdConlumn)) as users " +
                    "FROM " +
                    "$tuitTableName " +
                    "WHERE " +
                    "$tuitNewsIndexColumn = %d AND " +
                    "$tuitCreationDateColumn > \"%s-%s-%s 00:00:00\" AND " +
                    "$tuitCreationDateColumn < \"%s-%s-%s 00:00:00\""

        const val insertNewsTemplate =
            "INSERT INTO $dataSetNotLabeledTableName ($dataSetNotLabeledIdColumnName, $dataSetNotLabeledTitleColumn, $dataSetNotLabeledContentColumn) VALUES (%s, \"%s\", \"%s\")"
        const val insertUserTemplate =
            "INSERT INTO $userNotLabeledTableName ($userNotLabeledIdColumn, $userNotLabeledNameColumn, $userNotLabeledFollowersColumn, $userNotLabeledFriendsColumn) VALUES (%s, \"%s\", %s, %s)"
        const val insertTuitTemplate =
            "INSERT INTO $tuitTableName ($tuitIdColumn, $tuitCreationDateColumn, $tuitTextColumn, $tuitNewsIndexColumn, $tuitUserIdConlumn) VALUES (%s, \"%s\", \"%s\", %s, %s)"

        const val deleteDataSetNotLabeled = "DELETE FROM $dataSetNotLabeledTableName"
        const val deleteUserNotLabeled = "DELETE FROM $userNotLabeledTableName"
        const val deleteTuit = "DELETE FROM $tuitTableName"
    }
}