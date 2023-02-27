package ui

import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import tornadofx.View
import tornadofx.chooseFile
import ui.handler.SelectFileForLabeledDBController

class ParserWindow: View() {

    val selectedFileForLabeledDBController: SelectFileForLabeledDBController by inject()

    override val root : BorderPane by fxml("/ParserScreen.fxml")

    fun onParseDatasetLabeledClicked() {
        println("parser dataset labeled clicked")

        selectedFileForLabeledDBController.fileSelected(
            chooseFile(
                "testing",
                mutableListOf<FileChooser.ExtensionFilter>().toTypedArray()
            )[0]
        )
    }

    fun onParseDatasetNotLabeledClicked() {
        println("parser dataset not labeled clicked")
        // TODO: Add controler to parse a hydrated csv file with tuits regarding fake news
    }

    fun onBackToHomeclicked() {
        println("Back to home screen")

        replaceWith<HomeWindow>()
    }
}