package ui

import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import tornadofx.View
import tornadofx.chooseFile
import ui.handler.SelectFileForBatchResultParsing

class ExecuteBatchWindow: View() {

    val selectedFileForBatChResults: SelectFileForBatchResultParsing by inject()

    override val root : BorderPane by fxml("/ExecuteBatchScreen.fxml")

    //TODO: This function should be called after the execution of a batch process in Repast.
    // It is not clear yet if this could be done automatically or must be done manually.
    fun fileToParseBatchResults() {
        selectedFileForBatChResults.fileSelected(
            chooseFile(
                "Select Result From Batch",
                mutableListOf<FileChooser.ExtensionFilter>().toTypedArray()
            )[0]
        )
    }

    fun onBackToHomeclicked() {
        replaceWith<HomeWindow>()
    }
}