package ui

import database.databaseResults.db.DataBaseModelExecution
import database.databaselabeled.db.DataBaseGetInfo
import database.databaselabeled.db.DataBaseInsertions
import database.databasenotlabeled.db.DataBaseNotLabeled
import javafx.scene.control.ComboBox
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import tornadofx.View
import tornadofx.*
import ui.fragment.ResultForExecutionBatchWindow
import ui.fragment.ResultLabeledWindow
import ui.fragment.ResultNotLabeledWindow
import ui.handler.SelectFileForBatchResultParsing
import ui.handler.SelectFileForLabeledDBController

class ResultWindow: View() {




    override val root : BorderPane by fxml("/ResultScreen.fxml")

    private val datasetLabeledList: ComboBox<String> by fxid()
    private val datasetNotLabeledList: ComboBox<String> by fxid()
    private val resultSteps: ComboBox<String> by fxid()

    private val dataBaseGetInfo = DataBaseGetInfo()
    private val dataBaseInsertions = DataBaseInsertions()
    private val dataBAseNotLabeled = DataBaseNotLabeled()
    private val dataBaseModelExecution = DataBaseModelExecution()

    init {
        datasetLabeledList.items = dataBaseGetInfo.getDataSetList().toObservable()
        datasetNotLabeledList.items = dataBAseNotLabeled.getFakeNewsTitleForMostFakeNewsUsers().toObservable()
        resultSteps.items = dataBaseModelExecution.getConfigurationIds()?.toObservable()
    }



    fun selectedDataSetLabeledForResults() {
        find<ResultLabeledWindow>(mapOf("selectedDataset" to datasetLabeledList.selectedItem)).openWindow()
    }

    fun selectedDataSetNotLabeledForResults() {
        println("selectedDataset " + datasetNotLabeledList.selectedItem)
        find<ResultNotLabeledWindow>(mapOf("selectedDataset" to datasetNotLabeledList.selectedItem)).openWindow()
    }

    fun showExecutionSteps() {
        println("selectedConfig execution " + resultSteps.selectedItem)
        find<ResultForExecutionBatchWindow>(mapOf("configSelected" to resultSteps.selectedItem)).openWindow()
    }

    fun onBackToHomeclicked() {
        replaceWith<HomeWindow>()
    }

}