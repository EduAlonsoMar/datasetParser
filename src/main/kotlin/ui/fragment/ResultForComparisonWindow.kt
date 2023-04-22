package ui.fragment

import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.layout.BorderPane
import tornadofx.View
import ui.handler.ResultsChartCreationController

class ResultForComparisonWindow : View() {

    override val root: BorderPane by fxml("/ComparisonResultsScreen.fxml")

    private var dataSetNotLabeled: String? = null
    private var executionSelected: String? = null

    private val chartNotLabeled: LineChart<String, Number> by fxid()
    private val chartConfig: LineChart<String, Number> by fxid()

    private val resultsChartCreationController: ResultsChartCreationController by inject()

    private var seriesExecution: XYChart.Series<String, Number>? = null
    private var seriesDatasetNotLabeled: XYChart.Series<String, Number>? = null


    override fun onDock() {
        super.onDock()

        dataSetNotLabeled = params[DATASET_NOT_LABELED_PARAM] as? String
        executionSelected = params[EXECUTION_PARAM] as? String

        chartNotLabeled.title = "Dataset not labeled with id $dataSetNotLabeled"
        chartConfig.title = "Execution with id $executionSelected"

        executionSelected?.let { idSelected ->
            seriesExecution = XYChart.Series(
                "Users sharing fake news per hour",
                resultsChartCreationController.createUsersSharingFakesInResultSeries(idSelected)
            )
        }

        dataSetNotLabeled?.let { datasetNotLabeledId ->
            seriesDatasetNotLabeled = XYChart.Series(
                "Users sharing fake news per hour",
                resultsChartCreationController.createUsersSharingFromDatasetNotLabeledId(
                    Integer.parseInt(dataSetNotLabeled)
                )
            )
        }
        chartNotLabeled.data.addAll(seriesDatasetNotLabeled)
        chartConfig.data.addAll(seriesExecution)

    }

    override fun onUndock() {
        super.onUndock()

        chartConfig.data.removeAll(seriesExecution)
        chartNotLabeled.data.removeAll(seriesDatasetNotLabeled)
    }

    companion object {
        const val DATASET_NOT_LABELED_PARAM = "datasetNotLabeled"
        const val EXECUTION_PARAM = "execution"
    }
}


