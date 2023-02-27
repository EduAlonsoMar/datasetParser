package ui.fragment

import database.databasenotlabeled.timeline.GetTimeLineNotLabeled
import javafx.collections.ObservableList
import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.layout.BorderPane
import tornadofx.*

class ResultNotLabeledWindow : View("My View") {
    override val root : BorderPane by fxml("/ResultNotLabeledScreen.fxml")

    private var datasetSelected: String? = null

    private val resultChart : LineChart<String, Number> by fxid()

    private lateinit var getTimeLineLabeledUtils : GetTimeLineNotLabeled

    private val template = "%d"

    private lateinit var series1:  XYChart.Series<String, Number>

    override fun onDock() {
        super.onDock()
        println("On Dock")
        datasetSelected = params["selectedDataset"] as? String
        println(" Selected DataSet is ${params["configSelected"] as? String}")
        println(" Selected DataSet is $datasetSelected")

        //resultChart.removeFromParent()
        getTimeLineLabeledUtils = GetTimeLineNotLabeled(datasetSelected!!)

        resultChart.title = "Time evolution for $datasetSelected"
        series1 = XYChart.Series("Users sharing fake news per hour", createUsersSharingSeries())
        resultChart.data.addAll(series1)

    }

    private fun createUsersSharingSeries(): ObservableList<XYChart.Data<String, Number>> {
        val result = mutableListOf<XYChart.Data<String, Number>>().toObservable()

        var data: XYChart.Data<String, Number>
        var hours: String
        for (i in 0..30) {
            hours = String.format(template, i)
            data = XYChart.Data<String, Number>(hours, (getTimeLineLabeledUtils.getNumberOfUserSendingInAnHour(i) * 100) / getTimeLineLabeledUtils.totalUsers)
            result.add(data)
        }

        return result

    }
}
