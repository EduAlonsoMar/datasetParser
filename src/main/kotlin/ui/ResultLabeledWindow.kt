package ui

import database.databaselabeled.timeline.GetTimeLineLabeled
import javafx.collections.ObservableList
import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series
import javafx.scene.layout.BorderPane
import tornadofx.*

class ResultLabeledWindow : View("My View") {
    override val root : BorderPane by fxml("/ResultScreen.fxml")

    private var datasetSelected: String? = null

    private val resultChart : LineChart<String, Number> by fxid()

    private lateinit var getTimeLineLabeledUtils: GetTimeLineLabeled

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
        getTimeLineLabeledUtils = GetTimeLineLabeled(datasetSelected!!)

        resultChart.title = "Time evolution for $datasetSelected"
        series1 = Series("Believers", createBelieversSeries())
        series2 = Series("Deniers", createDeniersSeries())
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

    private fun createBelieversSeries(): ObservableList<XYChart.Data<String, Number>> {
        val result = mutableListOf<XYChart.Data<String, Number>>().toObservable()

        // println(" Total days: ${getTimeLineUtils.totalDays}")
        var data: XYChart.Data<String, Number>
        var days: String
        var value: Number
        for (i in 0..15) {
            value = i
            days = String.format(template, i)
            data = XYChart.Data<String, Number>(days, (getTimeLineLabeledUtils.getNumberOfBelieversForDay(i) * 100) / getTimeLineLabeledUtils.totalUsers)
            result.add(data)
        }

        return result

    }

    private fun createDeniersSeries(): ObservableList<XYChart.Data<String, Number>> {
        val result = mutableListOf<XYChart.Data<String, Number>>().toObservable()

        var data: XYChart.Data<String, Number>
        var days: String
        var value: Number
        for (i in 0..15) {
            value = i
            days = String.format(template, i)
            data = XYChart.Data<String, Number>(days, (getTimeLineLabeledUtils.getNumberOfDeniersForDay(i) * 100)/getTimeLineLabeledUtils.totalUsers)
            result.add(data)
        }
        return result

    }
}
