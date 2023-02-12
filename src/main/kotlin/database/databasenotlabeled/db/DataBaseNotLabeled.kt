package database.databasenotlabeled.db

import database.FakeNewsDataBase
import java.sql.SQLException
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList

class DataBaseNotLabeled: FakeNewsDataBase(jdbcUrl, user, pass) {

    fun getFakeNewsTitleForMostFakeNewsUsers(): ArrayList<String> {
        initializeConnection()
        val resultList = arrayListOf<String>()
        try {
            val stmt = connection?.createStatement()
            val result = stmt?.executeQuery(selectMostFakeNewsUsers)
            result?.let { resultSet ->
                while (resultSet.next()) {
                    resultList.add(resultSet.getString("title"))
                }
            }
        } catch (e: SQLException) {
            logQueryError(selectMostFakeNewsUsers, e)
        }

        return resultList
    }

    fun getFakeNewsIdFromTitle(title: String): Int {
        initializeConnection()
        val queryText = String.format(getFakeNewsIdBasedOnTitle, title)
        try {
            val stmt = connection?.createStatement()
            val result = stmt?.executeQuery(queryText)
            result?.let { resultSet ->
                if (resultSet.next()) {
                    return resultSet.getInt("id")
                }
            }
        } catch (e: SQLException) {
            logQueryError(queryText, e)
        }

        return 0
    }

    fun getEarliestDate(fakeNewsId: Int): Timestamp? {
        initializeConnection()
        val queryText = String.format(getEarliestDatForTuitInDataSet, fakeNewsId)
        try {
            val stmt = connection?.createStatement()
            val result = stmt?.executeQuery(queryText)
            result?.let { resultSet ->
                if (resultSet.next()) {
                    return resultSet.getTimestamp("creation_date")
                }
            }
        } catch (e: SQLException) {
            logQueryError(queryText, e)
        }

        return null
    }

    fun getUsersInHour(c: Calendar, fakeNewsId: Int): Int {
        initializeConnection()
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
            val stmt = connection?.createStatement()
            val result = stmt?.executeQuery(queryText)
            result?.let {resultSet ->
                if (resultSet.next()) {
                    return resultSet.getInt("users")
                }
            }
        } catch (e: SQLException) {
            logQueryError(queryText, e)
        }

        return 0
    }

    fun getTotalUsersForFakeNews(fakeNewsId: Int): Int {
        initializeConnection()
        val queryText = String.format(getNumberOfUsersForFakeNews, fakeNewsId)
        try {
            val stmt = connection?.createStatement()
            val result = stmt?.executeQuery(queryText)
            result?.let { resultSet ->
                if (resultSet.next()) {
                    return resultSet.getInt("users")
                }
            }
        } catch (e: SQLException) {
            logQueryError(queryText, e)
        }
        return 0
    }

    companion object {
        private const val jdbcUrl = "jdbc:mysql://localhost:3306/FakeNewsDataSet"
        private const val user = "fakenews"
        private const val pass = "fakenews"

        private const val selectMostFakeNewsUsers =
            "SELECT " +
                "FakeNewsDataSet.Tuit.news_index, " +
                "FakeNewsDataSet.News.title, " +
                "COUNT(FakeNewsDataSet.Tuit.news_index) AS news_occurrence " +
            "FROM " +
                "FakeNewsDataSet.Tuit, " +
                "FakeNewsDataSet.News " +
            "WHERE " +
                "FakeNewsDataSet.News.id = FakeNewsDataSet.Tuit.news_index " +
                "GROUP BY " +
                "FakeNewsDataSet.Tuit.news_index " +
            "ORDER BY " +
                "news_occurrence DESC " +
            "LIMIT 5"

        private const val getFakeNewsIdBasedOnTitle =
            "SELECT " +
                "FakeNewsDataSet.News.id " +
            "FROM " +
               "FakeNewsDataSet.News " +
            "WHERE " +
                "FakeNewsDataSet.News.title = \"%s\""

        private const val getNumberOfUsersForFakeNews =
            "SELECT " +
                "COUNT(distinct(user_idUser)) as users " +
            "FROM " +
                "FakeNewsDataSet.Tuit " +
            "WHERE " +
                "FakeNewsDataSet.Tuit.news_index = %d"

        private const val getEarliestDatForTuitInDataSet =
            "SELECT " +
                "FakeNewsDataSet.Tuit.creation_date " +
            "FROM " +
                "FakeNewsDataSet.Tuit " +
            "WHERE " +
                "FakeNewsDataSet.Tuit.news_index = %d " +
            "ORDER BY " +
                "creation_date " +
                "ASC"

        private const val getUsersInHour =
            "SELECT " +
                "COUNT(distinct(user_idUser)) as users " +
            "FROM " +
                "FakeNewsDataSet.Tuit " +
            "WHERE " +
                "FakeNewsDataSet.Tuit.news_index = %d AND " +
                "creation_date > \"%s-%s-%s %s:00:00\" AND " +
                "creation_date < \"%s-%s-%s %s:00:00\""
    }
}