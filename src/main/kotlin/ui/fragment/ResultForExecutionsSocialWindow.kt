package ui.fragment

import data.database.model.ConfigurationSocialFakeNews
import data.database.model.DatasetLabeled
import data.database.model.ErrorSocialForLabeled
import data.repository.DatasetLabeledRepository
import data.repository.ExecutionsResultsRepository
import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import tornadofx.View
import ui.handler.ResultsChartCreationController
import kotlin.properties.Delegates

class ResultForExecutionsSocialWindow : View() {
    override val root: BorderPane by fxml("/ResultForExecutionBatchScreenSocial.fxml")

    private val resultChartPercentages: LineChart<String, Number> by fxid()
    private val forShowError: TextField by fxid()
    private val forShowErrorLabeled: Label by fxid()
    private val resultsChartsController: ResultsChartCreationController by inject()

    private var configSelected: String? = params["configSelected"] as? String

    private var executionsResultsRepository = ExecutionsResultsRepository()
    private var datasetLabeledRepository = DatasetLabeledRepository()

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


        resultChartPercentages.title = "Users time evolution for $configSelected"

        configSelected?.let {

            series2 = XYChart.Series(
                "Percentage of believers per day",
                resultsChartsController.createBelieversInResultSocial(it)
            )
            series3 = XYChart.Series(
                "Percentage of deniers per day",
                resultsChartsController.createDeniersInResultSocial(it)
            )
        }

        // resultChartSharing.data.addAll(series1)

        resultChartPercentages.data.addAll(series2, series3)

    }

    private fun calculateNrmseLabeledSocial(
        config: ConfigurationSocialFakeNews,
        dataset: DatasetLabeled
    ): ErrorSocialForLabeled {
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
            percentageOfBelieversInStep = (executionsResultsRepository.getNumberOfBelieversInDaySocial(config.id.toString(), j).toDouble() * 100) / executionsResultsRepository.getTotalUsersSocial(config.id.toString())

            numberOfDeniersInDay = datasetLabeledRepository.getNumberOfDeniersForDay(dataset.id, i).toDouble()
            percentageOfDeniersInDataset = (numberOfDeniersInDay * 100) / datasetLabeledRepository.getTotalUsers(dataset.id)
            percentageOfDeniersInStep = (executionsResultsRepository.getNumberOfDeniersInDaySocial(config.id.toString(), j).toDouble() * 100) /executionsResultsRepository.getTotalUsersSocial(config.id.toString())

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
            j += executionsResultsRepository.normalizedAddedValueForLabeledSocial(config.id.toString())

        }

        val rmseBelievers = Math.sqrt(totalSumBelievers / 15)
        val rmseDeniers = Math.sqrt(totalSumDeniers / 15)
        val rmseTotal = Math.sqrt(rmseBelievers + rmseDeniers)
        val nrmseBelievers = rmseBelievers / (PmaxBelievers - PminBelievers)
        val nrmseDenying = rmseDeniers / (PmaxDeniers - PminDeniers)

        return ErrorSocialForLabeled(
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
        println("onCalculateErrorForLabeledClicked")
    }
    fun onCalculateErrorClicked() {
        //forShowError.text = calculateNrmseNotLabeled().toString()
        val configs = executionsResultsRepository.getConfigurationsSocial()

        //val list = dbHandler.getListOfExecutions()
        var i = 0
        for (config in configs) {
            percentageCompletion = "${(i * 100) / configs.size} % of executions completed"
            val listLabeled = datasetLabeledRepository.getDatasets()

            println("Calculated error not labeled")

            for (dataset in listLabeled) {
                executionsResultsRepository.insertErrorSocialLabeled(calculateNrmseLabeledSocial(config, dataset))
            }

            println("Calculated error labeled")
            i++

        }

        forShowError.text = "Done"
    }

    override fun onUndock() {
        super.onUndock()
        println("On undock")
        // resultChartSharing.data.removeAll(series1)
        resultChartPercentages.data.removeAll(series2, series3)
    }
}