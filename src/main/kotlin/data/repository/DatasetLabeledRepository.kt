package data.repository

import data.database.model.DatasetLabeled
import data.database.model.Opinion
import data.database.model.UserLabeled
import data.datasource.DatasetLabeledDataSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class DatasetLabeledRepository (): KoinComponent{


    private val datasetLabeledDataSource: DatasetLabeledDataSource by inject()

    fun insertDataSet(dataset: DatasetLabeled): Int {
        return datasetLabeledDataSource.insertNewDataset(dataset.name, dataset.description)
    }

    fun insertDataSetTimeRecords(user: UserLabeled, opinion: Opinion) {
        datasetLabeledDataSource.insertDatasetTimedRecords(user, opinion)
    }

    fun getDatasetListOnlyNames(): List<String> {
        return datasetLabeledDataSource.getDataSetList().map { datasetLabeled -> datasetLabeled.name }
    }

    fun getDatasetId(title: String): Int {
        return datasetLabeledDataSource.getDataSetId(title)
    }

    fun getTotalUsers(datasetId: Int): Int {
        return datasetLabeledDataSource.getNumberOfDistinctUsers(datasetId)
    }

    fun getDatasets(): ArrayList<DatasetLabeled> {
        return datasetLabeledDataSource.getDataSetList()
    }

//    fun totalDaysInDataSet(datasetToShowTimeline: String): Long {
//        val earliestDate: Date? = datasetLabeledDataSource.getEarliestDate(datasetToShowTimeline)
//        val oldestDate: Date? = datasetLabeledDataSource.getOldestDate(datasetToShowTimeline)
//
//        return if (earliestDate != null && oldestDate != null) {
//            (oldestDate.time - earliestDate.time) / 1000 / 60 / 60 / 24
//        } else {
//            0L
//        }
//    }


    private val users = mutableMapOf<Int, String>()

    fun getNumberOfBelieversForDay(datasetId: Int, dayInSequence: Int): Int {
        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val earliestDate: Date? = datasetLabeledDataSource.getEarliestDate(datasetId)
        try{
            c.time = earliestDate
        } catch (e: Exception) {
            println("Error treating calendar")
        }

        // println("time in calendar = ${sdf.format(c.time)}")

        c.add(Calendar.DAY_OF_MONTH, dayInSequence)

        // println("time in calendar = ${sdf.format(c.time)}")

        val result = datasetLabeledDataSource.getUsersInDay(c, datasetId)

        val believers = result.filter { map ->
            map.value == "endorses"
        }

        return believers.size
    }

    fun getNumberOfDeniersForDay(datasetToShowTimeline: Int, dayInSequence: Int): Int {
        val c = Calendar.getInstance()
        val earliestDate: Date? = datasetLabeledDataSource.getEarliestDate(datasetToShowTimeline)
        try{
            c.time = earliestDate
        } catch (e: Exception) {
            println("Error treating calendar")
        }

        c.add(Calendar.DAY_OF_MONTH, dayInSequence)

        val result = datasetLabeledDataSource.getUsersInDay(c, datasetToShowTimeline)

        val deniers = result.filter { map ->
            map.value == "denies"
        }

        return deniers.size
    }

}