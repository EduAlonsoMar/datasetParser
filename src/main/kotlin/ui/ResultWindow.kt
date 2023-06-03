package ui

import data.repository.DatasetLabeledRepository
import data.repository.DatasetNotLabeledRepository
import data.repository.ExecutionsResultsRepository
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.control.ComboBox
import javafx.scene.layout.BorderPane
import tornadofx.*
import ui.css.AppStyle
import ui.fragment.*
import ui.fragment.ResultForComparisonLabeledWindow.Companion.DATASET_LABELED_PARAM
import ui.fragment.ResultForComparisonWindow.Companion.DATASET_NOT_LABELED_PARAM
import ui.fragment.ResultForComparisonWindow.Companion.EXECUTION_PARAM

class ResultWindow : View() {

    private val datasetLabeledRepository = DatasetLabeledRepository()
    private val datasetNotLabeledRepository = DatasetNotLabeledRepository()
    private val executionsResultsRepository = ExecutionsResultsRepository()

    private val selectedDatasetNotLabeled = SimpleStringProperty()
    private val selectedDatasetLabeled = SimpleStringProperty()
    private val selectedDatasetLabeledSocial = SimpleStringProperty()

    private lateinit var datasetNotLabeledComboBox: ComboBox<String?>
    private lateinit var executionOSNComboBox: ComboBox<String?>

    private lateinit var datasetLabeledOSNComboBox: ComboBox<String?>
    private lateinit var executionOSNComboBoxLabeled: ComboBox<String?>

    private lateinit var datasetLabeledSocialComboBox: ComboBox<String?>
    private lateinit var executionSocialComboBoxLabeled: ComboBox<String?>

    override val root = scrollpane {
        content =
        borderpane {
            id = "MainWindow"
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
                        id = "hBoxComparison"
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
                    label("ResultSets From OSN Realistic model") {
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

                hbox {
                    addClass(AppStyle.parserLine)
                    label("ResultSets From Social Fake News model") {
                        addClass(AppStyle.regularText)
                    }
                }

                hbox {
                    addClass(AppStyle.parserLine)

                    val combo = combobox {
                        id = "resultSteps"
                        items = executionsResultsRepository.getConfigurationsSocialIds().toObservable()
                    }

                    button("Show Results") {
                        setOnAction {
                            find<ResultForExecutionsSocialWindow>(mapOf("configSelected" to combo.selectedItem)).openWindow()
                        }
                    }
                }

                vbox {
                    id = "vBoxInCenter"
                    addClass(AppStyle.verticalLayoutWithBorder)
                    hbox {
                        addClass(AppStyle.parserLine)
                        label("Comparison between datasets and executions FROM OSN Realistic Model") {
                            addClass(AppStyle.regularText)
                        }
                    }
                    vbox {
                        id = "vBoxInCenter2"
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
                            id = "hboxWithComparisonOSNNotLabeled"
                            var executionCombo = combobox {
                                addClass(AppStyle.textField)
                                id = "executionOSN"
                            }

                            val datasetNotLabeledCombo = combobox(
                                selectedDatasetNotLabeled,
                                datasetNotLabeledRepository.getTitleForDataSetsWithMostUsers().toObservable()
                            ) {
                                addClass(AppStyle.comboBoxWithLimit)
                                id = "dataSetNotLabeled"
                            }

                            button("Show Comparison").setOnAction {
                                find<ResultForComparisonWindow>(
                                    mapOf(
                                        DATASET_NOT_LABELED_PARAM to datasetNotLabeledCombo.selectedItem,
                                        EXECUTION_PARAM to executionCombo.selectedItem
                                    )
                                ).openWindow()
                            }

                        }
                        val executionLabeled = hbox {
                            label("execution id") {
                                addClass(AppStyle.textField)
                            }
                            label("Dataset labeled id") {
                                addClass(AppStyle.textField)
                            }

                        }

                        val datasetLabeled = hbox {
                            id = "hboxForOSNANDLabeled"
                            val execution = combobox {
                                addClass(AppStyle.textField)
                                id = "executionONSComboboxLabeled"
                            }

                            val datasetLabeled = combobox(
                                selectedDatasetLabeled,
                                datasetLabeledRepository.getDatasetListOnlyNames().toObservable()
                            ) {
                                addClass(AppStyle.textField)
                                id = "datasetOSNLabeledCombobox"
                            }

                            button("Show Comparison").setOnAction {
                                find<ResultForComparisonLabeledWindow>(
                                    mapOf(
                                        DATASET_LABELED_PARAM to datasetLabeled.selectedItem,
                                        ResultForComparisonLabeledWindow.EXECUTION_PARAM to execution.selectedItem
                                    )
                                ).openWindow()
                            }
                        }


                    }
                }

                vbox {
                    id = "vBoxInCenter3"
                    addClass(AppStyle.verticalLayoutWithBorder)
                    hbox {
                        addClass(AppStyle.parserLine)
                        label("Comparison between datasets and executions From Social Fake News model") {
                            addClass(AppStyle.regularText)
                        }
                    }
                    vbox {
                        id = "vBoxWithHorizontal"
                        addClass(AppStyle.parserLine)
                        val executionLabeled = hbox {
                            label("execution id") {
                                addClass(AppStyle.textField)
                            }
                            label("Dataset labeled id") {
                                addClass(AppStyle.textField)
                            }

                        }

                        val datasetLabeled = hbox {
                            id = "hboxWithTheCombos"
                            val execution = combobox {
                                addClass(AppStyle.textField)
                                id = "executionComboSocialLabeled"
                            }

                            val datasetLabeled = combobox(
                                selectedDatasetLabeledSocial,
                                datasetLabeledRepository.getDatasetListOnlyNames().toObservable()
                            ) {
                                addClass(AppStyle.comboBoxWithLimit)
                                id = "datasetLabeledSocial"
                            }

                            button("Show Comparison").setOnAction {
                                find<ResultForComparisonLabeledSocialWindow>(
                                    mapOf(
                                        DATASET_LABELED_PARAM to datasetLabeled.selectedItem,
                                        ResultForComparisonLabeledWindow.EXECUTION_PARAM to execution.selectedItem
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
    }

    init {

        val borderPane = (root.content as BorderPane)

        for (i in (borderPane.center.getChildList()!!)) {
            println("id: ${i.id} and type")
            if (i.id == "vBoxInCenter") {
                for (j in i.getChildList()!!) {
                    println("id: ${j.id}")
                    if (j.id == "vBoxInCenter2") {
                        for (k in j.getChildList()!!) {
                            println("id: ${k.id}")
                            if (k.id == "hboxWithComparisonOSNNotLabeled") {
                                for (l in k.getChildList()!!) {
                                    println("id: ${l.id}")
                                    if (l.id == "executionOSN") {
                                        executionOSNComboBox = (l as ComboBox<String?>)
                                    } else if (l.id == "dataSetNotLabeled") {
                                        datasetNotLabeledComboBox = l as ComboBox<String?>
                                    }
                                }
                            } else if (k.id == "hboxForOSNANDLabeled") {
                                for (m in k.getChildList()!!) {
                                    println("id: ${m.id}")
                                    if (m.id == "executionONSComboboxLabeled") {
                                        executionOSNComboBoxLabeled = (m as ComboBox<String?>)
                                    } else if (m.id == "datasetOSNLabeledCombobox") {
                                        datasetLabeledOSNComboBox = m as ComboBox<String?>
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (i.id == "vBoxInCenter3") {
                for (n in i.getChildList()!!) {
                    println("id ${n.id}")
                    if (n.id == "vBoxWithHorizontal") {
                        for (o in n.getChildList()!!) {
                            println("id ${n.id}")
                            if (o.id == "hboxWithTheCombos") {
                                for (p in o.getChildList()!!) {
                                    println("id ${p.id}")
                                    if (p.id == "executionComboSocialLabeled") {
                                        executionSocialComboBoxLabeled = p as ComboBox<String?>
                                    } else if (p.id == "datasetLabeledSocial") {
                                        datasetLabeledSocialComboBox = p as ComboBox<String?>
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

        selectedDatasetNotLabeled.onChange {
            executionOSNComboBox.items = executionsResultsRepository.getBestOSNConfigurationsForDataSetNotLabeled(
                datasetNotLabeledComboBox.selectedItem ?: "default"
            ).toObservable()
        }

        selectedDatasetLabeled.onChange {
            executionOSNComboBoxLabeled.items = executionsResultsRepository.getBestOSNConfigurationsForDataSetLabeled(
                datasetLabeledOSNComboBox.selectedItem ?: "default"
            ).toObservable()
        }

        selectedDatasetLabeledSocial.onChange {
            executionSocialComboBoxLabeled.items =
                executionsResultsRepository.getBestSocialConfigurationForDataSetLabeled(
                    datasetLabeledSocialComboBox.selectedItem ?: "default"
                ).toObservable()
        }
    }

    fun onBackToHomeclicked() {
        replaceWith<HomeWindow>()
    }

}