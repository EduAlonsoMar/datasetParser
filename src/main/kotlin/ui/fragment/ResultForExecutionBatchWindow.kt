package ui.fragment

import database.databaseResults.timeline.GetTimeLineExecutionResults
import database.databasenotlabeled.timeline.GetTimeLineNotLabeled
import javafx.collections.ObservableList
import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.layout.BorderPane
import tornadofx.View
import tornadofx.toObservable

class ResultForExecutionBatchWindow: View() {

    override val root : BorderPane by fxml("/ResultForExecutionBatchScreen.fxml")

    private val resultChart : LineChart<String, Number> by fxid()

    private var configSelected: String? = params["configSelected"] as? String

    private lateinit var getTimeLineLabeledUtils : GetTimeLineExecutionResults

    private val template = "%d"

    private lateinit var series1:  XYChart.Series<String, Number>

    override fun onDock() {
        super.onDock()
        println("On Dock")
        configSelected = params["configSelected"] as? String
        println(" Selected DataSet is ${params["selectedDataset"] as? String}")
        println(" Selected DataSet is $configSelected")

        //resultChart.removeFromParent()
        getTimeLineLabeledUtils = GetTimeLineExecutionResults(configSelected!!)

        resultChart.title = "Time evolution for $configSelected"
        series1 = XYChart.Series("Users sharing fake news per hour", createUsersSharingSeries())
        resultChart.data.addAll(series1)

    }


    private fun createUsersSharingSeries(): ObservableList<XYChart.Data<String, Number>> {
        val result = mutableListOf<XYChart.Data<String, Number>>().toObservable()

        var data: XYChart.Data<String, Number>
        var hours: String
        for (i in 0..29) {
            hours = String.format(template, i)
            data = XYChart.Data<String, Number>(hours, (getTimeLineLabeledUtils.getNumberOfUserSendingInAnHour(i+1) * 100) / (getTimeLineLabeledUtils.totalUsers ?: 1000))
            result.add(data)
        }

        return result

    }
}