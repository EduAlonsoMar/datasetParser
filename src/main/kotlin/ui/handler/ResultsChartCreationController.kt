package ui.handler

import data.repository.DatasetLabeledRepository
import data.repository.DatasetNotLabeledRepository
import data.repository.ExecutionsResultsRepository
import data.repository.ExecutionsResultsRepository.Companion.stepsForExecution
import javafx.collections.ObservableList
import javafx.scene.chart.XYChart
import tornadofx.Controller
import tornadofx.toObservable

class ResultsChartCreationController : Controller() {

    private val datasetLabeledRepository: DatasetLabeledRepository = DatasetLabeledRepository()
    private val datasetNotLabeledRepository = DatasetNotLabeledRepository()
    private val executionsResultsRepository = ExecutionsResultsRepository()

    fun createBelieversSeriesInLabeledDatasetId(datasetId: Int): ObservableList<XYChart.Data<String, Number>> {
        val result = mutableListOf<XYChart.Data<String, Number>>().toObservable()
        var data: XYChart.Data<String, Number>
        var days: String
        for (i in 0..15) {

            days = String.format(templateForXAxis, i)
            data = XYChart.Data<String, Number>(
                days,
                (datasetLabeledRepository.getNumberOfBelieversForDay(
                    datasetId,
                    i
                ) * 100) / datasetLabeledRepository.getTotalUsers(datasetId)
            )
            result.add(data)
        }

        return result
    }

    fun createBelieversSeriesInLabeledDatasetTitle(
        datasetTitle: String
    ): ObservableList<XYChart.Data<String, Number>> {
        return createBelieversSeriesInLabeledDatasetId(datasetLabeledRepository.getDatasetId(datasetTitle))
    }

    fun createDeniersSeriesInLabeledDatasetId(datasetId: Int): ObservableList<XYChart.Data<String, Number>> {
        val result = mutableListOf<XYChart.Data<String, Number>>().toObservable()
        var data: XYChart.Data<String, Number>
        var days: String
        for (i in 0..15) {
            days = String.format(templateForXAxis, i)
            data = XYChart.Data<String, Number>(
                days,
                (datasetLabeledRepository.getNumberOfDeniersForDay(
                    datasetId,
                    i
                ) * 100) / datasetLabeledRepository.getTotalUsers(datasetId)
            )
            result.add(data)
        }
        return result
    }

    fun createDeniersChartInLabeledDataset(datasetTitle: String): ObservableList<XYChart.Data<String, Number>> {
        return createDeniersSeriesInLabeledDatasetId(datasetLabeledRepository.getDatasetId(datasetTitle))
    }

    fun createUsersSharingFromDatasetNotLabeledTitle(datasetTitle: String): ObservableList<XYChart.Data<String, Number>> {
        val datasetId = datasetNotLabeledRepository.getDatasetId(datasetTitle)
        return createUsersSharingFromDatasetNotLabeledId(datasetId)
    }

    fun createUsersSharingFromDatasetNotLabeledId(datasetId: Int): ObservableList<XYChart.Data<String, Number>> {
        val result = mutableListOf<XYChart.Data<String, Number>>().toObservable()
        var data: XYChart.Data<String, Number>
        var hours: String
        for (i in 0..30) {
            hours = String.format(templateForXAxis, i)
            data = XYChart.Data<String, Number>(
                hours,
                (datasetNotLabeledRepository.getNumberOfUserSendingInAnHour(
                    datasetId,
                    i
                ) * 100) / datasetNotLabeledRepository.getTotalUsers(datasetId)
            )
            result.add(data)
        }

        return result
    }

    fun createUserSharingPerDayDatasetNotLabeled(datasetTitle: String): ObservableList<XYChart.Data<String, Number>> {
        val result = mutableListOf<XYChart.Data<String, Number>>().toObservable()
        val datasetId = datasetNotLabeledRepository.getDatasetId(datasetTitle)
        var data: XYChart.Data<String, Number>
        var days: String
        for (i in 0..30) {
            days = String.format(templateForXAxis, i)
            data = XYChart.Data<String, Number>(
                days,
                (datasetNotLabeledRepository.getNumberOfUsersSentingInDay(
                    datasetId,
                    i
                ) * 100) / datasetNotLabeledRepository.getTotalUsers(datasetId)
            )
            result.add(data)
        }
        return result

    }

    fun createUsersSharingFakesInResultSeries(configId: String): ObservableList<XYChart.Data<String, Number>> {
        val result = mutableListOf<XYChart.Data<String, Number>>().toObservable()

        var data: XYChart.Data<String, Number>
        var hours: String
        var i = 0
        while (i < stepsForExecution) {
            hours = String.format(templateForXAxis, i / 3)

            data = XYChart.Data<String, Number>(
                hours,
                (executionsResultsRepository.getNumberOfUserSendingInAnHour(
                    configId,
                    i
                ) * 100) / (executionsResultsRepository.getTotalUsers(configId))
            )

            result.add(data)
            i += 3
        }

        return result
    }

    fun createBelieversInResult(configId: String): ObservableList<XYChart.Data<String, Number>> {
        val result = mutableListOf<XYChart.Data<String, Number>>().toObservable()

        var data: XYChart.Data<String, Number>
        var days: String
        var i = 0
        while (i < stepsForExecution) {
            days = String.format(templateForXAxis, i / 6)

            data = XYChart.Data<String, Number>(
                days,
                (executionsResultsRepository.getNumberOfBelieversInDay(
                    configId,
                    i
                ) * 100) / (executionsResultsRepository.getTotalUsers(configId))
            )

            result.add(data)
            i += 6
        }

        return result
    }

    fun createDeniersInResult(configId: String): ObservableList<XYChart.Data<String, Number>> {
        val result = mutableListOf<XYChart.Data<String, Number>>().toObservable()

        var data: XYChart.Data<String, Number>
        var days: String
        var i = 0
        while (i < stepsForExecution) {
            days = String.format(templateForXAxis, i / 6)

            data = XYChart.Data<String, Number>(
                days,
                (executionsResultsRepository.getNumberOfDeniersInDay(
                    configId,
                    i
                ) * 100) / (executionsResultsRepository.getTotalUsers(configId))
            )

            result.add(data)
            i += 6
        }

        return result
    }

    companion object {
        private const val templateForXAxis = "%d"
    }
}