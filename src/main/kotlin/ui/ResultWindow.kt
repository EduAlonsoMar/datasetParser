package ui

import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.layout.BorderPane
import tornadofx.*

class ResultWindow : View("My View") {
    override val root : BorderPane by fxml("/ResultScreen.fxml")

    private val datasetSelected: String? = params["selectedDataset"] as? String

    val resultChart : LineChart<String, Number> by fxid()

    init {
        println(" Selected DataSet is $datasetSelected")

        resultChart.title = "Time evolution for $datasetSelected"
        resultChart.series("Believers") {
            data("day 1", 10)
            data("day 2", 20)
            data("day 3", 30)
        }
        resultChart.series("Deniers") {
            data("day 1", 20)
            data("day 2", 40)
            data("day 3", 60)
        }
    }
}
