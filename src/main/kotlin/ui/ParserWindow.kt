package ui

import javafx.stage.FileChooser
import tornadofx.*
import ui.css.AppStyle
import ui.handler.ParserController
import java.io.File

class ParserWindow : View() {

    private val parserController: ParserController by inject()

    private val textFieldLabeled = textfield {
        setMaxSize(50.0, 5.0)
    }
    private val textFieldNotLabeledNews = textfield {
        setMaxSize(50.0, 5.0)
    }


    // private val buttonExploreTuits: Button by fxid()
    // private val panel: BorderPane by fxid()
    //by fxml("/ParserScreen.fxml") {
    override val root = borderpane {

        top = label("Parser for data set records") {
            addClass(AppStyle.titleLabel)
        }
        left = vbox {
            minWidth = 20.0
        }

        center = vbox {
            hbox {
                addClass(AppStyle.parserLine)
                label("Select labeled csv file") {
                    addClass(AppStyle.regularText)
                }
                val textField = textfield {
                    addClass(AppStyle.textField)
                }
                button("Explore") {
                    addClass(AppStyle.exploreButton)
                    setOnAction {
                        val file = selectFile()
                        textField.text = file?.path
                        file?.let {
                            parserController.fileLabeledSelected(it)
                        }

                    }
                }
            }

            borderpane {
                addClass(AppStyle.parserGroup)

                top = hbox {
                    maxHeight = 10.0
                    addClass(AppStyle.divider)
                }

                left = hbox {
                    maxWidth = 10.0
                    addClass(AppStyle.divider)
                }

                right = hbox {
                    maxWidth = 10.0
                    addClass(AppStyle.divider)
                }

                bottom = hbox {
                    maxHeight = 10.0
                    addClass(AppStyle.divider)
                }

                center = vbox {
                    hbox {
                        addClass(AppStyle.parserLine)
                        label("Select not labeled fake news csv file") {
                            addClass(AppStyle.regularText)
                        }
                        val textField = textfield {
                            addClass(AppStyle.textField)
                        }
                        button("Explore") {
                            addClass(AppStyle.exploreButton)
                            setOnAction {
                                val file = selectFile()
                                textField.text = file?.path
                                file?.let {
                                    parserController.fileWithNewsNotLabeledSelected(it)
                                }

                            }
                        }
                    }

                    hbox {
                        addClass(AppStyle.parserLine)
                        label("Select not labeled tuits csv file") {
                            addClass(AppStyle.regularText)
                        }
                        val textField = textfield {
                            addClass(AppStyle.textField)
                        }
                        button("Explore") {
                            addClass(AppStyle.exploreButton)
                            setOnAction {
                                val file = selectFile()
                                textField.text = file?.path
                                file?.let {
                                    parserController.fileWithTuitsToParseSelected(it)
                                }

                            }
                        }
                    }

                    hbox {
                        addClass(AppStyle.parserLine)
                        label("Select tuits and news index csv file") {
                            addClass(AppStyle.regularText)
                        }
                        val textField = textfield {
                            addClass(AppStyle.textField)
                        }
                        button("Explore") {
                            addClass(AppStyle.exploreButton)
                            setOnAction {
                                val file = selectFile()
                                textField.text = file?.path
                                file?.let {
                                    parserController.fileWithTuitsAndNewsSelected(it)
                                }
                            }
                        }

                    }

                    hbox {
                        addClass(AppStyle.parserLine)
                        button("parse") {
                            setOnAction {
                                parent.group().show()
                                parserController.parseDatasetNotLabeledFiles()
                                parent.group().hide()
                            }
                        }
                    }
                }
            }

            borderpane {
                addClass(AppStyle.parserGroup)

                top = hbox {
                    maxHeight = 10.0
                    addClass(AppStyle.divider)
                }

                left = hbox {
                    maxWidth = 10.0
                    addClass(AppStyle.divider)
                }

                right = hbox {
                    maxWidth = 10.0
                    addClass(AppStyle.divider)
                }

                bottom = hbox {
                    maxHeight = 10.0
                    addClass(AppStyle.divider)
                }

                center = vbox {
                    hbox {
                        addClass(AppStyle.parserLine)
                        label("Select config file for OSN Model") {
                            addClass(AppStyle.regularText)
                        }
                        val textField = textfield {
                            addClass(AppStyle.textField)
                        }
                        button("Explore") {
                            addClass(AppStyle.exploreButton)
                            setOnAction {
                                val file = selectFile()
                                textField.text = file?.path
                                file?.let {
                                    parserController.fileWithConfigSelected(it)
                                }

                            }
                        }
                    }

                    hbox {
                        addClass(AppStyle.parserLine)
                        label("Select steps file from OSN Model") {
                            addClass(AppStyle.regularText)
                        }
                        val textField = textfield {
                            addClass(AppStyle.textField)
                        }
                        button("Explore") {
                            addClass(AppStyle.exploreButton)
                            setOnAction {
                                val file = selectFile()
                                textField.text = file?.path
                                file?.let {
                                    parserController.fileWithStepsSelected(it)
                                }

                            }
                        }
                    }

                    hbox {
                        addClass(AppStyle.parserLine)
                        button("parse") {
                            setOnAction {
                                parserController.parseExecutionResultsFilesFromOSNModel()
                            }
                        }
                    }
                }
            }

            borderpane {
                addClass(AppStyle.parserGroup)

                top = hbox {
                    maxHeight = 10.0
                    addClass(AppStyle.divider)
                }

                left = hbox {
                    maxWidth = 10.0
                    addClass(AppStyle.divider)
                }

                right = hbox {
                    maxWidth = 10.0
                    addClass(AppStyle.divider)
                }

                bottom = hbox {
                    maxHeight = 10.0
                    addClass(AppStyle.divider)
                }

                center = vbox {
                    hbox {
                        addClass(AppStyle.parserLine)
                        label("Select config file for Social Fake News Model") {
                            addClass(AppStyle.regularText)
                        }
                        val textField = textfield {
                            addClass(AppStyle.textField)
                        }
                        button("Explore") {
                            addClass(AppStyle.exploreButton)
                            setOnAction {
                                val file = selectFile()
                                textField.text = file?.path
                                file?.let {
                                    parserController.fileWithConfigSelected(it)
                                }

                            }
                        }
                    }

                    hbox {
                        addClass(AppStyle.parserLine)
                        label("Select steps file from Social Fake News Executions") {
                            addClass(AppStyle.regularText)
                        }
                        val textField = textfield {
                            addClass(AppStyle.textField)
                        }
                        button("Explore") {
                            addClass(AppStyle.exploreButton)
                            setOnAction {
                                val file = selectFile()
                                textField.text = file?.path
                                file?.let {
                                    parserController.fileWithStepsSelected(it)
                                }

                            }
                        }
                    }

                    hbox {
                        addClass(AppStyle.parserLine)
                        button("parse") {
                            setOnAction {
                                parserController.parseExecutionResultsFilesFromSocialFakeNewsModel()
                            }
                        }
                    }
                }
            }


            hbox {
                button("back") {
                    setOnAction {
                        onBackToHomeclicked()
                    }
                }
            }


        }


        addClass(AppStyle.appScreen)

    }

    override fun onDock() {
        super.onDock()
        println("Docking")
    }

    fun showLoading() {
        root.group().show()
    }

    fun hideLoading() {
        root.group().hide()
    }

//    fun onParseDatasetLabeledClicked() {
//        println("parser dataset labeled clicked")
//
//        try {
//            val fileSelected = chooseFile(
//                "testing",
//                mutableListOf<FileChooser.ExtensionFilter>().toTypedArray()
//            )[0]
//            selectedFileForLabeledDBController.fileSelected(
//                fileSelected
//            )
//            textFieldLabeled.text = fileSelected.path
//
//        } catch (e: Exception) {
//            alert(Alert.AlertType.ERROR, "Testing", buttons = arrayOf(ButtonType.OK), owner = this.currentWindow)
//        }
//    }
//
//    fun onParseDatasetNotLabeledWithNewsClicked() {
//        println("parser dataset not labeled clicke for news")
//        try {
//            selectedFileForNotLabeledDBController.fileWithNewsToParseSelected(
//                chooseFile(
//                    "testing",
//                    mutableListOf<FileChooser.ExtensionFilter>().toTypedArray()
//                )[0]
//            )
//        } catch (e: Exception) {
//            alert(Alert.AlertType.ERROR, "Testing", buttons = arrayOf(ButtonType.OK), owner = this.currentWindow)
//        }
//    }
//
//    fun onParseDatasetNotLabeledWithTuitsClicked() {
//        println("Parser dataset not labeled tuits clicked for tuits")
//        try {
//            selectedFileForNotLabeledDBController.fileWithTuitsToParseSelected(
//
//                chooseFile(
//                    "testing",
//                    mutableListOf<FileChooser.ExtensionFilter>().toTypedArray()
//                )[0]
//            )
//        } catch (e: Exception) {
//            alert(Alert.AlertType.ERROR, "Testing", buttons = arrayOf(ButtonType.OK), owner = this.currentWindow)
//        }
//
//
//    }

    private fun selectFile(): File? {
        try {
            return chooseFile(
                "testing",
                mutableListOf<FileChooser.ExtensionFilter>().toTypedArray()
            )[0]
        } catch (e: Exception) {
            return null
        }
    }

    fun onBackToHomeclicked() {
        println("Back to home screen")

        replaceWith<HomeWindow>()
    }
}