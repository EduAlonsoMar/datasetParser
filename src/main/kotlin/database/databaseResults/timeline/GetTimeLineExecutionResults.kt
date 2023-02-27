package database.databaseResults.timeline

import database.databaseResults.db.DataBaseModelExecution
import database.databasenotlabeled.db.DataBaseNotLabeled
import java.sql.Timestamp
import java.util.*

class GetTimeLineExecutionResults (var configId: String) {

    private val dataBaseHandler = DataBaseModelExecution()
    val totalUsers = dataBaseHandler.getTotalUsersForconfig(configId)

    fun getNumberOfUserSendingInAnHour(hourInSequence: Int): Int {

        return dataBaseHandler.getBeleiversInStep(configId, hourInSequence) ?: 0

    }
}