package ui

import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import javafx.stage.FileChooser
import javafx.stage.Stage
import ui.handler.DatasetTimedSelected

class MainWindow: Application() {

    private var datasetTimedSelected: DatasetTimedSelected? = null
    private var stage: Stage? = null

    private var textFieldOpenFile: TextField? = null

    private val eventHandler = EventHandler<ActionEvent>() {
        val fileChooserUI = FileChooser()
        val selectedFile = fileChooserUI.showOpenDialog(stage)
        selectedFile?.let {
            textFieldOpenFile?.text = selectedFile.path
            this@MainWindow.datasetTimedSelected?.fileSelected(it)
        }

        //Runtime.getRuntime().exec("open -R ./")

    }

    private val escuchador : () -> Unit = { println("Selected button") }

    private fun buttonClicked() {
        println("Selected button")
    }

    override fun start(stage: Stage) {
        this.stage = stage
        val javaVersion = System.getProperty("java.version")
        val javafxVersion = System.getProperty("javafx.version")
        val l = Label("Hello, JavaFX $javafxVersion, running on Java $javaVersion.")

        val labelOpenFile = Label("Open File")
        labelOpenFile.layoutX = 50.0
        labelOpenFile.layoutY = 50.0

        textFieldOpenFile = TextField(" ")
        textFieldOpenFile?.layoutX = 150.0
        textFieldOpenFile?.layoutY = 50.0
        textFieldOpenFile?.prefWidth = 375.0

        val buttonToExplore = Button("Explore")
        buttonToExplore.layoutX = 550.0
        buttonToExplore.layoutY = 50.0
        buttonToExplore.onAction = eventHandler

        val layout = Pane(buttonToExplore, labelOpenFile, textFieldOpenFile)
        val scene = Scene(layout, 640.0, 480.0)
        stage.scene = scene
        stage.show()
    }

    fun start(datasetTimedSelected: DatasetTimedSelected) {
        this.datasetTimedSelected = datasetTimedSelected
        launch(MainWindow::class.java)
    }

}