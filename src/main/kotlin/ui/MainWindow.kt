package ui

import datasetlabeled.db.DataBaseGetInfo
import datasetlabeled.db.DataBaseInsertions
import datasetlabeled.db.DataBaseLabeled
import javafx.scene.control.ComboBox
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import tornadofx.View
import tornadofx.*
import ui.handler.SelectFileForLabeledDBController

class MainWindow: View() {

    val selectedFileForLabeledDBController: SelectFileForLabeledDBController by inject()

    override val root : BorderPane by fxml("/MainScreen.fxml")

    private val datasetList: ComboBox<String> by fxid()

    init {
        datasetList.items = DataBaseGetInfo().getDataSetList()
    }

    fun fileToParseSelected() {
        selectedFileForLabeledDBController.fileSelected(
            chooseFile(
                "testing",
                mutableListOf<FileChooser.ExtensionFilter>().toTypedArray()
            )[0]
        )
    }

    fun selectedDataSetForResults() {
        find<ResultWindow>(mapOf("selectedDataset" to datasetList.selectedItem)).openWindow()
    }

}