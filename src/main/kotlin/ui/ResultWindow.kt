package ui

import data.repository.DatasetLabeledRepository
import data.repository.DatasetNotLabeledRepository
import data.repository.ExecutionsResultsRepository
import javafx.geometry.Pos
import tornadofx.*
import ui.css.AppStyle
import ui.fragment.ResultForComparisonWindow
import ui.fragment.ResultForExecutionBatchWindow
import ui.fragment.ResultLabeledWindow
import ui.fragment.ResultNotLabeledWindow

class ResultWindow : View() {

    private val datasetLabeledRepository = DatasetLabeledRepository()
    private val datasetNotLabeledRepository = DatasetNotLabeledRepository()
    private val executionsResultsRepository = ExecutionsResultsRepository()

    override val root = borderpane {
        addClass(AppStyle.resultScreen)
        top = hbox {
            addClass(AppStyle.titleDiv)
            label("Datasets and Execution Results") {
                addClass(AppStyle.titleLabel)
            }
        }

        center = vbox {
            addClass(AppStyle.containerWithPadding)
            hbox {
                addClass(AppStyle.verticalSeparator)
            }
            vbox {
                addClass(AppStyle.verticalLayoutWithBorder)
                hbox {
                    addClass(AppStyle.parserLine)
                    label("Datasets Labeled") {
                        addClass(AppStyle.regularText)
                    }
                }

                hbox {
                    addClass(AppStyle.parserLine)

                    val combo = combobox {
                        id = "datasetLabeledList"
                        items = datasetLabeledRepository.getDatasetListOnlyNames().toObservable()
                    }

                    button("Show Results") {
                        setOnAction {
                            find<ResultLabeledWindow>(mapOf("selectedDataset" to combo.selectedItem)).openWindow()
                        }
                    }

                }
            }
            hbox {
                addClass(AppStyle.verticalSeparator)
            }
            vbox {
                addClass(AppStyle.verticalLayoutWithBorder)
                hbox {
                    addClass(AppStyle.parserLine)
                    label("Datasets not labeled") {
                        addClass(AppStyle.regularText)
                    }
                }
                hbox {
                    addClass(AppStyle.parserLine)

                    val combo = combobox {
                        id = "datasetNotLabeledList"
                        items = datasetNotLabeledRepository.getTitleForDataSetsWithMostUsers().toObservable()
                    }

                    button("Show Results") {
                        setOnAction {
                            find<ResultNotLabeledWindow>(mapOf("selectedDataset" to combo.selectedItem)).openWindow()
                        }
                    }
                }
            }


            hbox {
                addClass(AppStyle.parserLine)
                label("ResultSets") {
                    addClass(AppStyle.regularText)
                }
            }

            hbox {
                addClass(AppStyle.parserLine)

                val combo = combobox {
                    id = "resultSteps"
                    items = executionsResultsRepository.getConfigurationIds().toObservable()
                }

                button("Show Results") {
                    setOnAction {
                        find<ResultForExecutionBatchWindow>(mapOf("configSelected" to combo.selectedItem)).openWindow()
                    }
                }
            }

            vbox {
                addClass(AppStyle.verticalLayoutWithBorder)
                hbox {
                    addClass(AppStyle.parserLine)
                    label("Comparison between datasets and executions") {
                        addClass(AppStyle.regularText)
                    }
                }
                vbox {
                    addClass(AppStyle.parserLine)
                    val execution = hbox {
                        label("execution id") {
                            addClass(AppStyle.textField)
                        }
                        label("Dataset not labeled id") {
                            addClass(AppStyle.textField)
                        }

                    }
                    val dataseNotLabeled = hbox {

                        val execution = textfield {
                            addClass(AppStyle.textField)
                        }

                        val datasetNotLabeled = textfield {
                            addClass(AppStyle.textField)
                        }

                        button("Show Comparison").setOnAction {
                            find<ResultForComparisonWindow>(
                                mapOf(
                                    "datasetNotLabeled" to datasetNotLabeled.text,
                                    "execution" to execution.text
                                )
                            ).openWindow()
                        }

                    }


                }
            }

            hbox {
                alignment = Pos.CENTER_RIGHT
                button("Back") {
                    setOnAction {
                        onBackToHomeclicked()
                    }
                }
            }
        }
    }


    fun onBackToHomeclicked() {
        replaceWith<HomeWindow>()
    }

}