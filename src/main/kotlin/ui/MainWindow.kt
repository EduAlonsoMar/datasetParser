package ui

import database.databaselabeled.db.DataBaseGetInfo
import database.databaselabeled.db.DataBaseInsertions
import database.databasenotlabeled.db.DataBaseNotLabeled
import javafx.scene.control.ComboBox
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import tornadofx.View
import tornadofx.*
import ui.handler.SelectFileForLabeledDBController

class MainWindow: View() {

    val selectedFileForLabeledDBController: SelectFileForLabeledDBController by inject()

    override val root : BorderPane by fxml("/MainScreen.fxml")

    private val datasetLabeledList: ComboBox<String> by fxid()
    private val datasetNotLabeledList: ComboBox<String> by fxid()

    private val dataBaseGetInfo = DataBaseGetInfo()
    private val dataBaseInsertions = DataBaseInsertions()
    private val dataBAseNotLabeled = DataBaseNotLabeled()

    init {
        datasetLabeledList.items = dataBaseGetInfo.getDataSetList().toObservable()
        datasetNotLabeledList.items = dataBAseNotLabeled.getFakeNewsTitleForMostFakeNewsUsers().toObservable()
    }

    fun fileToParseSelected() {
        selectedFileForLabeledDBController.fileSelected(
            chooseFile(
                "testing",
                mutableListOf<FileChooser.ExtensionFilter>().toTypedArray()
            )[0]
        )
    }

    fun selectedDataSetLabeledForResults() {
        find<ResultLabeledWindow>(mapOf("selectedDataset" to datasetLabeledList.selectedItem)).openWindow()
    }

    fun selectedDataSetNotLabeledForResults() {
        println("selectedDataset " + datasetNotLabeledList.selectedItem)
        find<ResultNotLabeledWindow>(mapOf("selectedDataset" to datasetNotLabeledList.selectedItem)).openWindow()
    }

}