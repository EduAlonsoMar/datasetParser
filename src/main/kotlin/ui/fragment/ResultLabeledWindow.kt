package ui.fragment


import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.layout.BorderPane
import tornadofx.View
import ui.handler.ResultsChartCreationController

class ResultLabeledWindow : View("My View") {
    override val root : BorderPane by fxml("/ResultLabeledScreen.fxml")

    private val resultsChartCreationController: ResultsChartCreationController by inject()

    private var datasetSelected: String? = null

    private val resultChart : LineChart<String, Number> by fxid()


    private val template = "%d"

    private lateinit var series1:  XYChart.Series<String, Number>
    private lateinit var series2: XYChart.Series<String, Number>

    override fun onRefresh() {
        super.onRefresh()
        println("On Refresh")
    }

    override fun onDock() {
        super.onDock()
        println("On Dock")
        datasetSelected = params["selectedDataset"] as? String
        println(" Selected DataSet is ${params["selectedDataset"] as? String}")
        println(" Selected DataSet is $datasetSelected")

        //resultChart.removeFromParent()


        resultChart.title = "Time evolution for $datasetSelected"
        datasetSelected?.let {
            series1 = XYChart.Series(
                "Believers",
                resultsChartCreationController.createBelieversSeriesInLabeledDatasetTitle(it))

            series2 = XYChart.Series(
                "Deniers",
                resultsChartCreationController.createDeniersChartInLabeledDataset(it)
            )
        }

        resultChart.data.addAll(series1, series2)

    }

    override fun onDelete() {
        super.onDelete()
        println("On Delete")
    }

    override fun onUndock() {
        super.onUndock()
        println("On undock")
        resultChart.data.removeAll(series1, series2)
    }

}
