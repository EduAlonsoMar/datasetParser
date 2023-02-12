package database.databaselabeled.timeline

import database.databaselabeled.db.DataBaseGetInfo
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Calendar

class GetTimeLineLabeled (private val datasetToShowTimeline: String){

    private val dataBaseHandler = DataBaseGetInfo()

    val totalUsers: Int = dataBaseHandler.getNumberOfDistinctUsers()

    private val earliestDate: Date? = dataBaseHandler.getEarliestDate(datasetToShowTimeline)

    private val oldestDate: Date? = dataBaseHandler.getOldestDate(datasetToShowTimeline)

    val totalDays: Long = if (earliestDate != null && oldestDate != null) {
        (oldestDate.time - earliestDate.time) / 1000 / 60 / 60 / 24
    } else {
        0L
    }

    private val users = mutableMapOf<Int, String>()

    fun getNumberOfBelieversForDay(dayInSequence: Int): Int {
        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        try{
            c.time = earliestDate
        } catch (e: Exception) {
            println("Error treating calendar")
        }

        // println("time in calendar = ${sdf.format(c.time)}")

        c.add(Calendar.DAY_OF_MONTH, dayInSequence)

        // println("time in calendar = ${sdf.format(c.time)}")

        val result = dataBaseHandler.getUsersInDay(c, this.datasetToShowTimeline)

        val believers = result.filter { map ->
            map.value == "endorses"
        }

        return believers.size
    }

    fun getNumberOfDeniersForDay(dayInSequence: Int): Int {
        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        try{
            c.time = earliestDate
        } catch (e: Exception) {
            println("Error treating calendar")
        }

        // println("time in calendar = ${sdf.format(c.time)}")

        c.add(Calendar.DAY_OF_MONTH, dayInSequence)

        // println("time in calendar = ${sdf.format(c.time)}")

        val result = dataBaseHandler.getUsersInDay(c, datasetToShowTimeline)

        val deniers = result.filter { map ->
            map.value == "denies"
        }

        return deniers.size
    }

}