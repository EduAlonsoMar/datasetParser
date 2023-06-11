package ui.fragment

import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.layout.BorderPane
import tornadofx.*
import ui.css.AppStyle
import ui.handler.ExecutionsDataController
import ui.handler.ResultsChartCreationController

class ResultForComparisonLabeledWindow : View() {

    override val root: BorderPane by fxml("/ComparisonResultsLabeledScreen.fxml")

    private var dataSetLabeled: String? = null
    private var executionSelected: String? = null

    private val chartLabeled: LineChart<String, Number> by fxid()
    private val configData: BorderPane by fxid()

    private val resultsChartCreationController: ResultsChartCreationController by inject()
    private val executionsDataController: ExecutionsDataController by inject()

    private var seriesExecutionBelievers: XYChart.Series<String, Number>? = null
    private var seriesExecutionDeniers: XYChart.Series<String, Number>? = null
    private var seriesBelievers: XYChart.Series<String, Number>? = null
    private var seriesDeniers: XYChart.Series<String, Number>? = null

    private fun createChart() {
        chartLabeled.title = "Dataset not labeled $dataSetLabeled execution OSN $executionSelected"

        dataSetLabeled?.let { datasetSelectedId ->
            seriesBelievers = XYChart.Series(
                "% Believers per day in dataset",
                resultsChartCreationController.createBelieversSeriesInLabeledDatasetTitle(
                    datasetSelectedId
                )
            )

            seriesDeniers = XYChart.Series(
                "% Deniers per day in dataset",
                resultsChartCreationController.createDeniersChartInLabeledDataset(
                    datasetSelectedId
                )
            )
        }

        executionSelected?.let { idSelected ->
            seriesExecutionBelievers = XYChart.Series(
                "% Believers per day in model",
                resultsChartCreationController.createBelieversInResult(idSelected)
            )

            seriesExecutionDeniers = XYChart.Series(
                "% Deniers per day in model",
                resultsChartCreationController.createDeniersInResult(idSelected)
            )
        }

        chartLabeled.data.addAll(seriesBelievers, seriesDeniers, seriesExecutionBelievers, seriesExecutionDeniers)
    }

    private fun showExecutionInfo() {
        val configOSN = executionsDataController.getConfigOSN(executionSelected)

        configData.center = hbox {
            vbox {
                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Agents: "
                    }
                    label {
                        text = configOSN.agents
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Influencers: "
                    }
                    label {
                        text = configOSN.influencers
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Bots: "
                    }
                    label {
                        text = configOSN.bots
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Work with Time dynamics: "
                    }
                    label {
                        text = configOSN.workWithTimeDynamics
                    }
                }

            }

            vbox {
                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Time access for common users: "
                    }
                    label {
                        text = configOSN.timeAccessForCommonUsers
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Time access for bots: "
                    }
                    label {
                        text = configOSN.timeAccessForBots
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Initial nodes in barabasi: "
                    }
                    label {
                        text = configOSN.initialNodesBarabasi
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Nodes in barabasi: "
                    }
                    label {
                        text = configOSN.nodesInBarabasi
                    }
                }

            }

            vbox {
                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Number of initial believers: "
                    }
                    label {
                        text = configOSN.numberOfInitialBelievers
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "PVulnerability: "
                    }
                    label {
                        text = configOSN.vulnerabilityMean
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Precovery: "
                    }
                    label {
                        text = configOSN.recoveryMean
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "PShare: "
                    }
                    label {
                        text = configOSN.sharingMean
                    }
                }

            }

            vbox {
                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Number of ticks: "
                    }
                    label {
                        text = configOSN.numberOfTicks
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "P_share_debunking: "
                    }
                    label {
                        text = configOSN.sharingDebunking
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Ticks to start loosing interest: "
                    }
                    label {
                        text = configOSN.ticksToStartLosingInterest
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "seed: "
                    }
                    label {
                        text = configOSN.seed
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Total nrmse: "
                    }
                    label {
                        text = executionsDataController.getNrsmeTotalOSN(
                            dataSetLabeled!!,
                            Integer.parseInt(executionSelected)
                        ).toString()
                    }
                }

            }

        }
    }

    override fun onDock() {
        super.onDock()

        dataSetLabeled = params[DATASET_LABELED_PARAM] as? String
        executionSelected = params[EXECUTION_PARAM] as? String

        createChart()
        showExecutionInfo()

    }

    override fun onUndock() {
        super.onUndock()

        chartLabeled.data.removeAll(seriesBelievers, seriesDeniers, seriesExecutionBelievers, seriesExecutionDeniers)
    }


    companion object {
        const val DATASET_LABELED_PARAM = "datasetLabeled"
        const val EXECUTION_PARAM = "execution"
    }
}