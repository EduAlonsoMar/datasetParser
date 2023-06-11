package ui.fragment

import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.layout.BorderPane
import tornadofx.View
import ui.handler.ResultsChartCreationController

class ResultNotLabeledWindow : View("My View") {
    override val root : BorderPane by fxml("/ResultNotLabeledScreen.fxml")

    private val resultsChartCreationController: ResultsChartCreationController by inject()

    private var datasetSelected: String? = null

    private val resultChartHours : LineChart<String, Number> by fxid()
    private val resultChartDays : LineChart<String, Number> by fxid()

    private lateinit var seriesHours: XYChart.Series<String, Number>
    private lateinit var seriesDays: XYChart.Series<String, Number>

    override fun onDock() {
        super.onDock()
        println("On Dock")
        datasetSelected = params["selectedDataset"] as? String
        println(" Selected DataSet is ${params["configSelected"] as? String}")
        println(" Selected DataSet is $datasetSelected")

        //resultChart.removeFromParent()

        datasetSelected?.let {
            seriesHours = XYChart.Series("Users sharing fake news per hour", resultsChartCreationController.createUsersSharingFromDatasetNotLabeledTitle(it, false))
            seriesDays = XYChart.Series("Users sharing fake news per day", resultsChartCreationController.createUsersSharingFromDatasetNotLabeledTitle(it, true))
        }
        resultChartHours.title = "Time evolution for $datasetSelected"

        resultChartHours.data.addAll(seriesHours)

        resultChartDays.title = "Time evolution for $datasetSelected"
        resultChartDays.data.addAll(seriesDays)

    }

    override fun onUndock() {
        super.onUndock()
        println("On undock")
        resultChartHours.data.removeAll(seriesHours)
        resultChartDays.data.removeAll(seriesDays)
    }
}
