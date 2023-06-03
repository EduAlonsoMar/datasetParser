package ui.fragment


import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import tornadofx.*
import ui.css.AppStyle
import ui.handler.ExecutionsDataController
import ui.handler.ResultsChartCreationController

class ResultForComparisonLabeledSocialWindow: View() {

    override val root: BorderPane by fxml("/ComparisonResultsLabeledScreen.fxml")

    private var dataSetLabeled: String? = null
    private var executionSelected: String? = null

    private val chartLabeled: LineChart<String, Number> by fxid()
    private val configData: BorderPane by fxid()
    // private val chartConfig: LineChart<String, Number> by fxid()

    private val resultsChartCreationController: ResultsChartCreationController by inject()
    private val executionsDataController: ExecutionsDataController by inject()

    private var seriesExecutionBelievers: XYChart.Series<String, Number>? = null
    private var seriesExecutionDeniers: XYChart.Series<String, Number>? = null
    private var seriesBelievers: XYChart.Series<String, Number>? = null
    private var seriesDeniers: XYChart.Series<String, Number>? = null


    private fun createChart() {
        chartLabeled.title = "Dataset $dataSetLabeled and execution $executionSelected"

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
                resultsChartCreationController.createBelieversInResultSocial(idSelected)
            )

            seriesExecutionDeniers = XYChart.Series(
                "% Deniers per day in model",
                resultsChartCreationController.createDeniersInResultSocial(idSelected)
            )
        }

        chartLabeled.data.addAll(seriesBelievers, seriesDeniers, seriesExecutionBelievers, seriesExecutionDeniers)
    }

    private fun showExecutionInfo() {
        val configSocial = executionsDataController.getConfigSocial(executionSelected)

        configData.center = hbox {
            vbox {
                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "believers count: "
                    }
                    label {
                        text = configSocial.believersCount
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Deniers count: "
                    }
                    label {
                        text = configSocial.deniersCount
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Susceptible Count: "
                    }
                    label {
                        text = configSocial.susceptibleCount
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Followers average: "
                    }
                    label {
                        text = configSocial.averageFollowers
                    }
                }

            }

            vbox {
                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Selected Topology: "
                    }
                    label {
                        text = configSocial.selectedTopology
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Number of total ticks: "
                    }
                    label {
                        text = configSocial.numberOfTicks
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Total Agents: "
                    }
                    label {
                        text = configSocial.totalAgents
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Bots Count: "
                    }
                    label {
                        text = configSocial.nBots
                    }
                }

            }

            vbox {
                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Believer Influencers Count: "
                    }
                    label {
                        text = configSocial.nOfInfluencersBelievers
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Deniers Influencers Count: "
                    }
                    label {
                        text = configSocial.nOfInfluencersDeniers
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Susceptible Influencers Count: "
                    }
                    label {
                        text = configSocial.nOfInfluencersSusceptibles
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Number of Followers to be Influencer: "
                    }
                    label {
                        text = configSocial.nFollowersToBeInfluencer
                    }
                }

            }

            vbox {
                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Number of connections per Bot: "
                    }
                    label {
                        text = configSocial.nBotsConnections
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "P_Infl: "
                    }
                    label {
                        text = configSocial.pInfl
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "P_Beleieve: "
                    }
                    label {
                        text = configSocial.pbelieve
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "P_Deny: "
                    }
                    label {
                        text = configSocial.pDeny
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "P_Vacc: "
                    }
                    label {
                        text = configSocial.pVacc
                    }
                }

                hbox {
                    addClass(
                        AppStyle.parserLine
                    )
                    label {
                        text = "Seed: "
                    }
                    label {
                        text = configSocial.seed
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