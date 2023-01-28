package ui

import datasetlabeled.DataBaseLabeled
import datasetlabeled.ParserDataSetTimed
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import javafx.stage.Stage
import tornadofx.View
import tornadofx.*
import ui.handler.DatasetTimedSelected
import java.io.File


class DefaultDatasetTimedSelected : DatasetTimedSelected {

    lateinit var parser : ParserDataSetTimed

    override fun fileSelected(file: File) {
        println("File selected ${file.path}")
        parser = ParserDataSetTimed(file)
        parser.insertRecordsInDataBase()

    }

}
class MainWindow: View() {

    private val datasetTimedSelected = DefaultDatasetTimedSelected()

    override val root : BorderPane by fxml("/MainScreen.fxml")

    val datasetList: ComboBox<String> by fxid()

    init {
        datasetList.items = DataBaseLabeled().getDataSetList()
    }

    fun fileToParseSelected() {
        datasetTimedSelected.fileSelected(chooseFile("testing", mutableListOf<FileChooser.ExtensionFilter>().toTypedArray()).get(0))
    }

    fun selectedDataSetForResults() {
        find<ResultWindow>(mapOf("selectedDataset" to datasetList.selectedItem)).openWindow()
    }

    /*override val root = hbox {
        label("Open file") {
            hboxConstraints {
                marginRight = 20.0
                hGrow = Priority.ALWAYS
            }
        }
        textfield("") {
            hboxConstraints {
                marginRight = 20.0
                hGrow = Priority.ALWAYS
            }
        }
        button("Explore") {
            hboxConstraints {
                marginRight = 20.0
                hGrow = Priority.ALWAYS
            }
        }.setOnAction {
            datasetTimedSelected.fileSelected(chooseFile("testing", mutableListOf<FileChooser.ExtensionFilter>().toTypedArray()).get(0))
        }

    }*/

    /*private var stage: Stage? = null

    private var textFieldOpenFile: TextField? = null*/

    /*private val eventHandler = EventHandler<ActionEvent>() {
        val fileChooserUI = FileChooser()
        val selectedFile = fileChooserUI.showOpenDialog()
        selectedFile?.let {
            textFieldOpenFile?.text = it.path
            this@MainWindow.datasetTimedSelected?.fileSelected(it)
        }

        //Runtime.getRuntime().exec("open -R ./")

    }*/

    //private val escuchador : () -> Unit = { println("Selected button") }

    /*private fun buttonClicked() {
        println("Selected button")
    }*/

    /*override fun start(stage: Stage) {
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
    }*/

    /*fun start(datasetTimedSelected: DatasetTimedSelected) {
        this.datasetTimedSelected = datasetTimedSelected
        launch(MainWindow::class.java)
    }*/

}