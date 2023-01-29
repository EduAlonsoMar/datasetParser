package ui

import datasetlabeled.timeline.GetTimeLine
import javafx.collections.ObservableList
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.layout.BorderPane
import tornadofx.*

class ResultWindow : View("My View") {
    override val root : BorderPane by fxml("/ResultScreen.fxml")

    private val datasetSelected: String? = params["selectedDataset"] as? String

    private val resultChart : LineChart<String, Number> by fxid()

    private val getTimeLineUtils = GetTimeLine(datasetSelected!!)

    private val template = "day %d"

    init {
        println(" Selected DataSet is $datasetSelected")

        resultChart.title = "Time evolution for $datasetSelected"
        resultChart.series("Believers", createBelieversSeries())
        resultChart.series("Deniers", createDeniersSeries())
    }

    private fun createBelieversSeries(): ObservableList<XYChart.Data<String, Number>> {
        val result = mutableListOf<XYChart.Data<String, Number>>().toObservable()

        println(" Total days: ${getTimeLineUtils.totalDays}")
        var data: XYChart.Data<String, Number>
        var days: String
        var value: Number
        for (i in 0..15) {
            value = i
            days = String.format(template, i)
            data = XYChart.Data<String, Number>(days, (getTimeLineUtils.getNumberOfBelieversForDay(i) * 100) / getTimeLineUtils.totalUsers)
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
            data = XYChart.Data<String, Number>(days, (getTimeLineUtils.getNumberOfDeniersForDay(i) * 100)/getTimeLineUtils.totalUsers)
            result.add(data)
        }
        return result

    }
}
