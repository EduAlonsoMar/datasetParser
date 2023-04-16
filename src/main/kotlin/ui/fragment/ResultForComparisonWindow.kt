package ui.fragment

import javafx.scene.chart.LineChart
import javafx.scene.layout.BorderPane
import tornadofx.View
import ui.handler.ResultsChartCreationController

class ResultForComparisonWindow: View() {

    override val root : BorderPane by fxml("/ComparisonResultsScreen.fxml")

    private var dataSetNotLabeled: String? = null
    private var executionSelected: String? = null

    private val chartNotLabeled: LineChart<String, Number> by fxid()
    private val chartConfig: LineChart<String, Number> by fxid()

    private val resultsChartCreationController: ResultsChartCreationController by inject()

    override fun onDock() {
        super.onDock()

        dataSetNotLabeled = params["datasetNotLabeled"] as? String
        executionSelected = params["execution"] as? String

        chartNotLabeled.title = "Dataset not labeled with id $dataSetNotLabeled"
        chartConfig.title = "Execution with id $executionSelected"

        dataSetNotLabeled?.let {
            chartNotLabeled.data.addAll(
                javafx.scene.chart.XYChart.Series("Users sharing fake news per hour", resultsChartCreationController.createUsersSharingFromDatasetNotLabeledId(
                    java.lang.Integer.parseInt(it))))
        }

        executionSelected?.let{
            chartConfig.data.addAll(
                javafx.scene.chart.XYChart.Series(
                    "Users sharing fake news per hour",
                    resultsChartCreationController.createUsersSharingFakesInResultSeries(it)
                ))
        }

    }

    override fun onUndock() {
        super.onUndock()

        chartNotLabeled.data.removeAll()
        chartConfig.data.removeAll()
    }
}


