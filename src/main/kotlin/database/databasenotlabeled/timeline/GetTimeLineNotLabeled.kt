package database.databasenotlabeled.timeline

import database.databasenotlabeled.db.DataBaseNotLabeled
import java.sql.Date
import java.sql.Timestamp
import java.util.Calendar

class GetTimeLineNotLabeled (datasetToShowTimeline: String) {

    private val dataBaseHandler = DataBaseNotLabeled()
    private val fakeNewsId = dataBaseHandler.getFakeNewsIdFromTitle(datasetToShowTimeline)
    val totalUsers = dataBaseHandler.getTotalUsersForFakeNews(fakeNewsId)
    private val earliestDate: Timestamp? = dataBaseHandler.getEarliestDate(fakeNewsId)


    fun getNumberOfUserSendingInAnHour(hourInSequence: Int): Int {
        val c = Calendar.getInstance()
        try{
            c.time = earliestDate
        } catch (e: Exception) {
            println("Error treating calendar")
        }

        c.add(Calendar.HOUR_OF_DAY, hourInSequence)

        return dataBaseHandler.getUsersInHour(c, fakeNewsId)

    }
}