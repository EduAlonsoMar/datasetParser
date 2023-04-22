package ui.fragment


import data.database.model.*
import data.repository.DatasetLabeledRepository
import data.repository.DatasetNotLabeledRepository
import data.repository.ExecutionsResultsRepository
import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import tornadofx.View
import ui.handler.ResultsChartCreationController
import kotlin.properties.Delegates

class ResultForExecutionBatchWindow : View() {

    override val root: BorderPane by fxml("/ResultForExecutionBatchScreen.fxml")

    private val resultChartSharing: LineChart<String, Number> by fxid()
    private val resultChartPercentages: LineChart<String, Number> by fxid()
    private val forShowError: TextField by fxid()
    private val forShowErrorLabeled: Label by fxid()
    private val resultsChartsController: ResultsChartCreationController by inject()

    private var configSelected: String? = params["configSelected"] as? String

    private var executionsResultsRepository = ExecutionsResultsRepository()
    private var datasetNotLabeledRepository = DatasetNotLabeledRepository()
    private var datasetLabeledRepository = DatasetLabeledRepository()

    private lateinit var series1: XYChart.Series<String, Number>
    private lateinit var series2: XYChart.Series<String, Number>
    private lateinit var series3: XYChart.Series<String, Number>

    private var percentageCompletion: String by Delegates.observable("") { _, _, newValue ->
        forShowErrorLabeled.text = newValue
    }

    override fun onDock() {
        super.onDock()
        println("On Dock")
        configSelected = params["configSelected"] as? String
        println(" Selected DataSet is ${params["selectedDataset"] as? String}")
        println(" Selected DataSet is $configSelected")

        //resultChart.removeFromParent()

        resultChartSharing.title = "Time evolution for $configSelected"
        resultChartPercentages.title = "Users time evolution for $configSelected"

        configSelected?.let {
            series1 = XYChart.Series(
                "Users sharing fake news per hour",
                resultsChartsController.createUsersSharingFakesInResultSeries(it)
            )
            series2 = XYChart.Series(
                "Percentage of believers per day",
                resultsChartsController.createBelieversInResult(it)
            )
            series3 = XYChart.Series(
                "Percentage of believers per day",
                resultsChartsController.createDeniersInResult(it)
            )
        }

        resultChartSharing.data.addAll(series1)
        resultChartPercentages.data.addAll(series2, series3)

    }


    private fun calculateNrmseNotLabeled(config: Configuration, dataset: DatasetNotLabeled): ErrorForNotLabeled {
        var i = 0
        var j = 0
        var percentageOfBelieversSharingInStep: Double
        var percentageOfBelieversSahringinDataset: Double
        var totalSum = 0.0
        var Pmax = 0.0
        var Pmin = 0.0
        var numberUsersSharingInAnHour: Double
        while (j < 30) {
            numberUsersSharingInAnHour = datasetNotLabeledRepository.getNumberOfUserSendingInAnHour(Integer.parseInt(dataset.id), j).toDouble()
            percentageOfBelieversSharingInStep =
                (executionsResultsRepository.getNumberOfUserSendingInAnHour(config.id.toString(), i).toDouble() * 100) / 1000
            percentageOfBelieversSahringinDataset = (numberUsersSharingInAnHour * 100) / datasetNotLabeledRepository.getTotalUsers(Integer.parseInt(dataset.id))
            if (i == 0) {
                Pmin = percentageOfBelieversSahringinDataset
            }
            totalSum += Math.pow(percentageOfBelieversSharingInStep - percentageOfBelieversSahringinDataset, 2.0)
            if (Pmax < percentageOfBelieversSahringinDataset) {
                Pmax = percentageOfBelieversSahringinDataset
            }
            if (Pmin > percentageOfBelieversSahringinDataset) {
                Pmin = percentageOfBelieversSahringinDataset
            }
            i += ExecutionsResultsRepository.normalizerForNotLabeled
            j++
        }
        val rmse = Math.sqrt(totalSum / 30)
        return ErrorForNotLabeled(
            configurationId = config.id!!,
            datasetNotLabeledId = Integer.parseInt(dataset.id),
            rmse,
            rmse / (Pmax - Pmin))
    }

    private fun calculateNrmseLabeled(config: Configuration, dataset: DatasetLabeled): ErrorForLabeled {
        var i = 0
        var j = 0
        var numberOfBelieversInDay: Double
        var percentageOfBelieversInStep: Double
        var percentageOfBelieversInDataset: Double
        var numberOfDeniersInDay: Double
        var percentageOfDeniersInStep: Double
        var percentageOfDeniersInDataset: Double

        var totalSumBelievers = 0.0
        var totalSumDeniers = 0.0

        var PmaxBelievers = 0.0
        var PminBelievers = 0.0
        var PmaxDeniers = 0.0
        var PminDeniers = 0.0
        while (i < 15) {
            numberOfBelieversInDay = datasetLabeledRepository.getNumberOfBelieversForDay(dataset.id!!, i).toDouble()
            percentageOfBelieversInDataset = (numberOfBelieversInDay * 100) / datasetLabeledRepository.getTotalUsers(dataset.id)
            percentageOfBelieversInStep = (executionsResultsRepository.getNumberOfBelieversInDay(config.id.toString(), j).toDouble() * 100) / executionsResultsRepository.getTotalUsers(config.id.toString())

            numberOfDeniersInDay = datasetLabeledRepository.getNumberOfDeniersForDay(dataset.id, i).toDouble()
            percentageOfDeniersInDataset = (numberOfDeniersInDay * 100) / datasetLabeledRepository.getTotalUsers(dataset.id)
            percentageOfDeniersInStep = (executionsResultsRepository.getNumberOfDeniersInDay(config.id.toString(), j).toDouble() * 100) /executionsResultsRepository.getTotalUsers(config.id.toString())

            if (i == 0) {
                PminBelievers = percentageOfBelieversInDataset
                PminDeniers = percentageOfDeniersInDataset
            }

            totalSumBelievers += Math.pow((percentageOfBelieversInDataset - percentageOfBelieversInStep), 2.0)
            totalSumDeniers += Math.pow((percentageOfDeniersInDataset - percentageOfDeniersInStep), 2.0)

            if (PmaxBelievers < percentageOfBelieversInDataset) {
                PmaxBelievers = percentageOfBelieversInDataset
            }

            if (PmaxDeniers < percentageOfDeniersInDataset) {
                PmaxDeniers = percentageOfDeniersInDataset
            }

            if (PminBelievers > percentageOfBelieversInDataset) {
                PminBelievers = percentageOfBelieversInDataset
            }

            if (PminDeniers > percentageOfDeniersInDataset) {
                PminDeniers = percentageOfDeniersInDataset
            }

            i++
            j += ExecutionsResultsRepository.normalizedForLabeled

        }

        val rmseBelievers = Math.sqrt(totalSumBelievers / 15)
        val rmseDeniers = Math.sqrt(totalSumDeniers / 15)
        val rmseTotal = Math.sqrt(rmseBelievers + rmseDeniers)
        val nrmseBelievers = rmseBelievers / (PmaxBelievers - PminBelievers)
        val nrmseDenying = rmseDeniers / (PmaxDeniers - PminDeniers)

        return ErrorForLabeled(
            configId = config.id,
            DataSetLabeledId = dataset.id,
            rmseBelievers = rmseBelievers,
            rmseDeniers = rmseDeniers,
            nmseBelievers = nrmseBelievers,
            nmseDeniers = nrmseDenying,
            rmseTotal = rmseTotal
        )


    }

    fun onCalculateErrorForLabeledClicked() {
        val configs = executionsResultsRepository.getConfigurations()

        for ((i, config) in configs.withIndex()) {
            percentageCompletion = "${(i * 100) / configs.size} % of executions completed"
            val listLabeled = datasetLabeledRepository.getDatasets()

            for (dataset in listLabeled) {
                executionsResultsRepository.insertErrorLabeled(calculateNrmseLabeled(config, dataset))
            }

            println("Calculated error labeled $i")
        }
        forShowErrorLabeled.text = "Done"
    }

    fun onCalculateErrorClicked() {
        //forShowError.text = calculateNrmseNotLabeled().toString()
        val configs = executionsResultsRepository.getConfigurations()

        //val list = dbHandler.getListOfExecutions()
        var i = 0
        for (config in configs) {
            percentageCompletion = "${(i * 100) / configs.size} % of executions completed"
            val listNotLabeled = datasetNotLabeledRepository.getDataSets()
            val listLabeled = datasetLabeledRepository.getDatasets()
            for (dataset in listNotLabeled) {
                executionsResultsRepository.insertErrorNotLabeled(calculateNrmseNotLabeled(config, dataset))
            }

            println("Calculated error not labeled")

            for (dataset in listLabeled) {
                executionsResultsRepository.insertErrorLabeled(calculateNrmseLabeled(config, dataset))
            }

            println("Calculated error labeled")
            i++

        }

        forShowError.text = "Done"
    }

    override fun onUndock() {
        super.onUndock()
        println("On undock")
        resultChartSharing.data.removeAll(series1)
        resultChartPercentages.data.removeAll(series2, series3)
    }
}