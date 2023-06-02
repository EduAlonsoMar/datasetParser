package ui.fragment

import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.layout.BorderPane
import tornadofx.View
import ui.handler.ResultsChartCreationController

class ResultForComparisonLabeledWindow: View() {

    override val root: BorderPane by fxml("/ComparisonResultsLabeledScreen.fxml")

    private var dataSetLabeled: String? = null
    private var executionSelected: String? = null

    private val chartLabeled: LineChart<String, Number> by fxid()
    // private val chartConfig: LineChart<String, Number> by fxid()

    private val resultsChartCreationController: ResultsChartCreationController by inject()

    private var seriesExecutionBelievers: XYChart.Series<String, Number>? = null
    private var seriesExecutionDeniers: XYChart.Series<String, Number>? = null
    private var seriesBelievers: XYChart.Series<String, Number>? = null
    private var seriesDeniers: XYChart.Series<String, Number>? = null

    override fun onDock() {
        super.onDock()

        dataSetLabeled = params[DATASET_LABELED_PARAM] as? String
        executionSelected = params[EXECUTION_PARAM] as? String

        chartLabeled.title = "Dataset not labeled $dataSetLabeled execution OSN $executionSelected"

        dataSetLabeled?.let { datasetSelectedId ->
            seriesBelievers = XYChart.Series(
                "% Believers per day in dataset",
                resultsChartCreationController.createBelieversSeriesInLabeledDatasetTitle(
                    datasetSelectedId
                )
            )

            seriesDeniers = XYChart.Series(
                "% Deniers per day in dataset",
                resultsChartCreationController.createDeniersChartInLabeledDataset(
                    datasetSelectedId
                )
            )
        }

        executionSelected?.let { idSelected ->
            seriesExecutionBelievers = XYChart.Series(
                "% Believers per day in model",
                resultsChartCreationController.createBelieversInResult(idSelected)
            )

            seriesExecutionDeniers = XYChart.Series(
                "% Deniers per day in model",
                resultsChartCreationController.createDeniersInResult(idSelected)
            )
        }

        chartLabeled.data.addAll(seriesBelievers, seriesDeniers, seriesExecutionBelievers, seriesExecutionDeniers)
    }

    override fun onUndock() {
        super.onUndock()

        chartLabeled.data.removeAll(seriesBelievers, seriesDeniers, seriesExecutionBelievers, seriesExecutionDeniers)
    }


    companion object {
        const val DATASET_LABELED_PARAM = "datasetLabeled"
        const val EXECUTION_PARAM = "execution"
    }
}