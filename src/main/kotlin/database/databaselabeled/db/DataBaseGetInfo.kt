package database.databaselabeled.db

import javafx.collections.ObservableList
import tornadofx.observableListOf
import java.sql.Date
import java.sql.SQLException
import java.sql.Statement
import java.util.*

class DataBaseGetInfo: DataBaseLabeled() {

    fun getNumberOfDistinctUsers(): Int {
        val query = "SELECT distinct count(distinct(id)) as users FROM DataSetLabeled.user"

        initializeConnection()

        val stmt: Statement? = connection?.createStatement()

        stmt?.let { statement ->
            val res = statement.executeQuery(query)
            if (res.next()) {
                return res.getInt("users")
            }

        }

        return 0
    }

    fun getEarliestDate(dataSetName: String): Date? {
        val query = String.format(
            "SELECT Min(date) as earliestDate FROM DataSetLabeled.opinion WHERE datasetId = %s",
            getDataSetId(dataSetName)
        )

        initializeConnection()

        val stmt: Statement? = connection?.createStatement()

        stmt?.let { statement ->
            val res = statement.executeQuery(query)
            if (res.next()) {
                return res.getDate("earliestDate")
            }
        }

        return null
    }

    fun getOldestDate(datasetName: String): Date? {
        val query = String.format(
            "SELECT Max(date) as oldestDate FROM DataSetLabeled.opinion WHERE datasetId = %s",
            getDataSetId(datasetName)
        )

        initializeConnection()

        val stmt: Statement? = connection?.createStatement()

        stmt?.let { statement ->
            val res = statement.executeQuery(query)
            if (res.next()) {
                return res.getDate("oldestDate")
            }
        }

        return null
    }

    fun getDataSetList(): ArrayList<String> {
        val query = "SELECT name FROM dataset"
        initializeConnection()
        val stmt = connection?.createStatement()
        val result = arrayListOf<String>()
        stmt?.let {statement ->
            try {
                val rs = statement.executeQuery(query)
                while(rs.next()) {
                    result.add(rs.getString("name"))
                }
            } catch (e: SQLException) {
                logQueryError(query, e)
            }
        }

        return result
    }

    private fun getNumberOfUsersInDayDependingOnLabel(day: Calendar, label: String): Int? {
        val template = "SELECT distinct count(distinct(userId)) as users FROM opinion WHERE date <= \"%s-%s-%s\" and label = \"%s\""
        val query = String.format(
            template,
            day.get(Calendar.YEAR),
            day.get(Calendar.MONTH) + 1,
            day.get(Calendar.DAY_OF_MONTH),
            label
            )
        initializeConnection()

        val smt = connection?.createStatement()
        smt?.let { statement ->
            try {
                val rs = statement.executeQuery(query)
                while(rs.next()) {
                    return rs.getInt("users")
                }
            } catch (e: SQLException) {
                logQueryError(query, e)
            }
        }

        return null
    }

    fun getUsersInDay(day: Calendar, dataSetName: String): Map<Int, String> {
        val template = "SELECT userid, label FROM opinion WHERE date <= \"%s-%s-%s\" and datasetId = %s ORDER BY date"
        val query = String.format(
            template,
            day.get(Calendar.YEAR),
            day.get(Calendar.MONTH) + 1,
            day.get(Calendar.DAY_OF_MONTH),
            getDataSetId(dataSetName)
        )
        val result = mutableMapOf<Int,String>()
        initializeConnection()

        val smt = connection?.createStatement()
        smt?.let { statement ->
            try {
                val rs = statement.executeQuery(query)
                while(rs.next()) {
                    if (result.containsKey(rs.getInt("userId"))) {
                        result.replace(rs.getInt("userId"), rs.getString("label"))
                    } else {
                        result[rs.getInt("userId")] = rs.getString("label")
                    }
                }
            } catch (e: SQLException) {
                logQueryError(query, e)
            }
        }

        return result
    }
}