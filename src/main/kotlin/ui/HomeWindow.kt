package ui

import javafx.scene.layout.BorderPane
import tornadofx.View
import tornadofx.ViewTransition
import tornadofx.seconds

class HomeWindow: View() {

    override val root : BorderPane by fxml("/HomeScreen.fxml")

    fun onParseDatasetClicked() {
        replaceWith(ParserWindow::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
    }

    /*fun onExecBatchClicked() {
        println("execBatchClicked")
        replaceWith<ExecuteBatchWindow>()
    }*/

    fun onShowResultsClicked() {
        println("Show Results clicked")
        replaceWith(ResultWindow::class)
    }
}