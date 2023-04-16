package data.repository

import data.database.model.DatasetNotLabeled
import data.database.model.Tuit
import data.database.model.UserNotLabeled
import data.datasource.DatasetNotLabeledDataSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class DatasetNotLabeledRepository (): KoinComponent {

    private val datasetNotLabeledDataSource: DatasetNotLabeledDataSource by inject()


//    private val fakeNewsId = datasetNotLabeledDataSource.getFakeNewsIdFromTitle(datasetToShowTimeline)
//    val totalUsers = datasetNotLabeledDataSource.getTotalUsersForFakeNews(fakeNewsId)
//    private val earliestDate: Timestamp? = datasetNotLabeledDataSource.getEarliestDate(fakeNewsId)

    fun insertDatasetNotLabeled(dataset: DatasetNotLabeled) {
        datasetNotLabeledDataSource.insertDatasetNotLabeled(dataset)
    }

    fun insertUserNotLabeled(user: UserNotLabeled) {
        datasetNotLabeledDataSource.insertUser(user)
    }

    fun insertTuit(tuit: Tuit) {
        datasetNotLabeledDataSource.insertTuit(tuit)
    }

    fun getTitleForDataSetsWithMostUsers(): List<String> {
        return datasetNotLabeledDataSource.getFakeNewsWithMostFakeNewsUsers().map { it.title }
    }

    fun getDataSets(): List<DatasetNotLabeled> {
        return datasetNotLabeledDataSource.getFakeNewsWithMostFakeNewsUsers()
    }
    fun getDatasetId(datasetTitle: String): Int {
        return datasetNotLabeledDataSource.getFakeNewsIdFromTitle(datasetTitle)
    }

    fun getNumberOfUserSendingInAnHour(datasetId: Int, hourInSequence: Int): Int {
        val c = Calendar.getInstance()
        try{
            c.time = datasetNotLabeledDataSource.getEarliestDate(datasetId)
        } catch (e: Exception) {
            println("Error treating calendar")
        }

        c.add(Calendar.HOUR_OF_DAY, hourInSequence)

        return datasetNotLabeledDataSource.getUsersInHour(c, datasetId)

    }

    fun getNumberOfUsersSentingInDay(datasetId: Int, dayInSequence: Int): Int {
        val c = Calendar.getInstance()
        try {
            c.time = datasetNotLabeledDataSource.getEarliestDate(datasetId)
        } catch (e: Exception) {
            println("Error treating calendar")
        }

        c.add(Calendar.DAY_OF_MONTH, dayInSequence)

        return datasetNotLabeledDataSource.getUsersInDay(c, datasetId)
    }

    fun getTotalUsers(datasetId: Int): Int {
        return datasetNotLabeledDataSource.getTotalUsersForFakeNews(datasetId)
    }
}