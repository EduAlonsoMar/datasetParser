package ui.handler

import data.database.model.*
import data.parser.DatasetLabeledParser
import data.parser.DatasetNotLabeledParser
import data.parser.ExecutionsParser
import data.parser.model.TuitNewsRecord
import data.repository.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import tornadofx.Controller
import java.io.File

class ParserController : Controller() {

    private val datasetLabeledRepository: DatasetLabeledRepository = DatasetLabeledRepository()
    private val datasetNotLabeledRepository = DatasetNotLabeledRepository()
    private val executionsResultsRepository = ExecutionsResultsRepository()
    private val datasetLabeledParserRepository = DatasetLabeledParserRepository()
    private val datasetNotLabeledParserRepository = DatasetNotLabeledParserRepository()
    private val executionsParserRepository = ExecutionsParserRepository()


    private var fileWithNewsToParse: File? = null
    private var fileWithTuitsToParse: File? = null
    private var fileWithTuitsAndNewsToParse: File? = null

    private var fileWithSteps: File? = null
    private var fileWithConfig: File? = null

    private var _parsingDatasetLabeledPercentage = MutableStateFlow<Int?>(null)
    val parsingDatasetLabeledPercentage = _parsingDatasetLabeledPercentage as StateFlow<Int?>


    fun fileLabeledSelected(file: File) {
        println("File selected ${file.path}")
        val parser = datasetLabeledParserRepository.getParserForFile(file)

        val datasetLabeled = DatasetLabeled(name = parser.datasetName)
        val datasetId = datasetLabeledRepository.insertDataSet(datasetLabeled)

        var user: UserLabeled
        var opinion: Opinion
        for (record in parser.parser) {
            user = UserLabeled(record.get(DatasetLabeledParser.userIdColumn))
            opinion = Opinion(
                record.get(DatasetLabeledParser.dateColumn),
                record.get(DatasetLabeledParser.userIdColumn),
                record.get(DatasetLabeledParser.labelColumn),
                datasetId
            )
            datasetLabeledRepository.insertDataSetTimeRecords(user, opinion)
        }

    }

    fun fileWithNewsNotLabeledSelected(file: File) {
        println("File selected ${file.path}")
        fileWithNewsToParse = file

    }

    fun fileWithTuitsToParseSelected(file: File) {
        println("File selected ${file.path}")
        fileWithTuitsToParse = file
    }

    fun fileWithTuitsAndNewsSelected(file: File) {
        println("File with news and tuits selected ${file.path}")
        fileWithTuitsAndNewsToParse = file
    }


    private fun convertCSVParserIntoAList(csvParser: CSVParser): ArrayList<TuitNewsRecord> {
        println("Converting csv file to a list")
        val list = arrayListOf<TuitNewsRecord>()
        for (record in csvParser) {
            list.add(
                TuitNewsRecord(
                    record.get(DatasetNotLabeledParser.newsAndTuitsNewsIndexColumn),
                    record.get(DatasetNotLabeledParser.newsAndTuitsTuitIdColumn)
                )
            )
        }

        return list
    }

    private fun searchNewsId(tuitId: String, csvParserNewsTuits: ArrayList<TuitNewsRecord>): String? {

        val iterator = csvParserNewsTuits.iterator()
        var tuitInNews: String
        var tmp: CSVRecord
        var i: Int
        for (record in csvParserNewsTuits) {
            if (record.tuitId == tuitId) {
                println("Found tuit $tuitId")
                return record.newsId
            }
        }
        return null
    }

    fun parseDatasetNotLabeledFiles() {
        if (fileWithNewsToParse == null ||
            fileWithTuitsToParse == null ||
            fileWithTuitsAndNewsToParse == null
        ) {
            println("all three files for dataset not labeled must me present")
            return
        }

        val parser = datasetNotLabeledParserRepository.getNotLabeledParser(
            fileWithNewsToParse!!,
            fileWithTuitsToParse!!,
            fileWithTuitsAndNewsToParse!!
        )

        for (record in parser.parserWithNews) {
            datasetNotLabeledRepository.insertDatasetNotLabeled(
                DatasetNotLabeled(
                    record.get(0),
                    record.get(DatasetNotLabeledParser.datasetNotLabeledTitleColumn),
                    record.get(DatasetNotLabeledParser.datasetNotLabeledContentColumn)
                )
            )
        }

        val listRelatingTuitsWithNews = convertCSVParserIntoAList(parser.parserWithTuitsAndNews)
        var user: UserNotLabeled
        var id: String
        for (record in parser.parserWithTuits) {
            id = record.get(DatasetNotLabeledParser.tuitsIdColumn)
            searchNewsId(id, listRelatingTuitsWithNews)?.let { newsId ->
                user = UserNotLabeled(
                    record.get(DatasetNotLabeledParser.tuitsUserIdColumn),
                    record.get(DatasetNotLabeledParser.tuitsUserNameColumn),
                    record.get(DatasetNotLabeledParser.tuitsFollowersColumn),
                    record.get(DatasetNotLabeledParser.tuitsFriendsClolumn)
                )
                datasetNotLabeledRepository.insertUserNotLabeled(user)


                datasetNotLabeledRepository.insertTuit(
                    Tuit(
                        id,
                        record.get(DatasetNotLabeledParser.tuitsTextColumn),
                        newsId,
                        user.id,
                        record.get(DatasetNotLabeledParser.tuitsTimestampColumn)
                    )
                )
            }

        }


    }

    fun fileWithConfigSelected(file: File) {
        fileWithConfig = file
    }

    fun fileWithStepsSelected(file: File) {
        fileWithSteps = file
    }

    private fun calculateNrmse(config: ConfigurationOSN, dataset: DatasetNotLabeled, isForDays: Boolean): PairOfErrorsForNotLabeled {
        var i = 0
        var j = 0
        var percentageOfBelieversSharingInStep: Double
        var percentageOfBelieversSahringinDataset: Double
        var totalSum = 0.0
        var Pmax = 0.0
        var Pmin = 0.0
        var numberUsersSharingInAnHour: Double
        while (j < 30) {
            numberUsersSharingInAnHour = if (isForDays) {
                datasetNotLabeledRepository.getNumberOfUsersSentingInDay(Integer.parseInt(dataset.id), j).toDouble()
            } else {
                datasetNotLabeledRepository.getNumberOfUserSendingInAnHour(Integer.parseInt(dataset.id), j).toDouble()
            }
            percentageOfBelieversSharingInStep =
                (executionsResultsRepository.getNumberOfUserSendingInAnHour(config.id.toString(), i).toDouble() * 100) / executionsResultsRepository.getTotalUsers(config.id.toString())
            percentageOfBelieversSahringinDataset = (numberUsersSharingInAnHour * 100) / datasetNotLabeledRepository.getTotalUsers(Integer.parseInt(dataset.id))
            if (i == 0) {
                Pmin = percentageOfBelieversSahringinDataset
            }
            totalSum += Math.pow(percentageOfBelieversSharingInStep - percentageOfBelieversSahringinDataset, 2.0)
            if (Pmax < percentageOfBelieversSahringinDataset) {
                Pmax = percentageOfBelieversSahringinDataset
            }
            if (Pmin > percentageOfBelieversSahringinDataset) {
                Pmin = percentageOfBelieversSahringinDataset
            }
            i += ExecutionsResultsRepository.normalizerForNotLabeled
            j++
        }
        val rmse = Math.sqrt(totalSum / 30)
        val nrmse = rmse / (Pmax - Pmin)

        return PairOfErrorsForNotLabeled(
                rmse = rmse,
                nrmse = nrmse
            )
    }
    private fun calculateNrmseNotLabeled(config: ConfigurationOSN, dataset: DatasetNotLabeled): ErrorForNotLabeled {
        val errorsForNotLabeledHours = calculateNrmse(config, dataset, false)
        val errorsForNotLabeledDays = calculateNrmse(config, dataset, true)


        return ErrorForNotLabeled(
            configurationId = config.id!!,
            datasetNotLabeledId = Integer.parseInt(dataset.id),
            errorsForNotLabeledHours.rmse,
            errorsForNotLabeledHours.nrmse,
            errorsForNotLabeledDays.rmse,
            errorsForNotLabeledDays.nrmse)
    }

    private fun calculateNrmseLabeled(config: ConfigurationOSN, dataset: DatasetLabeled): ErrorForLabeled {
        var i = 0
        var j = 0
        var numberOfBelieversInDay: Double
        var percentageOfBelieversInStep: Double
        var percentageOfBelieversInDataset: Double
        var numberOfDeniersInDay: Double
        var percentageOfDeniersInStep: Double
        var percentageOfDeniersInDataset: Double

        var totalSumBelievers = 0.0
        var totalSumDeniers = 0.0

        var PmaxBelievers = 0.0
        var PminBelievers = 0.0
        var PmaxDeniers = 0.0
        var PminDeniers = 0.0
        while (i < 15) {
            numberOfBelieversInDay = datasetLabeledRepository.getNumberOfBelieversForDay(dataset.id!!, i).toDouble()
            percentageOfBelieversInDataset = (numberOfBelieversInDay * 100) / datasetLabeledRepository.getTotalUsers(dataset.id)
            percentageOfBelieversInStep = (executionsResultsRepository.getNumberOfBelieversInDay(config.id.toString(), j).toDouble() * 100) / executionsResultsRepository.getTotalUsers(config.id.toString())

            numberOfDeniersInDay = datasetLabeledRepository.getNumberOfDeniersForDay(dataset.id, i).toDouble()
            percentageOfDeniersInDataset = (numberOfDeniersInDay * 100) / datasetLabeledRepository.getTotalUsers(dataset.id)
            percentageOfDeniersInStep = (executionsResultsRepository.getNumberOfDeniersInDay(config.id.toString(), j).toDouble() * 100) /executionsResultsRepository.getTotalUsers(config.id.toString())

            if (i == 0) {
                PminBelievers = percentageOfBelieversInDataset
                PminDeniers = percentageOfDeniersInDataset
            }

            totalSumBelievers += Math.pow((percentageOfBelieversInDataset - percentageOfBelieversInStep), 2.0)
            totalSumDeniers += Math.pow((percentageOfDeniersInDataset - percentageOfDeniersInStep), 2.0)

            if (PmaxBelievers < percentageOfBelieversInDataset) {
                PmaxBelievers = percentageOfBelieversInDataset
            }

            if (PmaxDeniers < percentageOfDeniersInDataset) {
                PmaxDeniers = percentageOfDeniersInDataset
            }

            if (PminBelievers > percentageOfBelieversInDataset) {
                PminBelievers = percentageOfBelieversInDataset
            }

            if (PminDeniers > percentageOfDeniersInDataset) {
                PminDeniers = percentageOfDeniersInDataset
            }

            i++
            j += ExecutionsResultsRepository.normalizedForLabeled

        }

        val rmseBelievers = Math.sqrt(totalSumBelievers / 15)
        val rmseDeniers = Math.sqrt(totalSumDeniers / 15)
        val rmseTotal = Math.sqrt(rmseBelievers + rmseDeniers)
        val nrmseBelievers = rmseBelievers / (PmaxBelievers - PminBelievers)
        val nrmseDenying = rmseDeniers / (PmaxDeniers - PminDeniers)

        return ErrorForLabeled(
            configId = config.id,
            DataSetLabeledId = dataset.id,
            rmseBelievers = rmseBelievers,
            rmseDeniers = rmseDeniers,
            nmseBelievers = nrmseBelievers,
            nmseDeniers = nrmseDenying,
            rmseTotal = rmseTotal
        )


    }

    fun parseExecutionResultsFilesFromOSNModel() {
        if (fileWithConfig == null ||
            fileWithSteps == null
        ) {
            println("All files for execution result parsing must be present")
            return
        }

        val parser = executionsParserRepository.getParser(
            fileWithConfig = fileWithConfig!!,
            fileWithSteps = fileWithSteps!!
        )

        val keyMapForConfigIds = mutableMapOf<String, Int>()
        var configId: Int
        for (record in parser.parserForConfig) {
            configId = executionsResultsRepository.insertConfigurationOSN(
                ConfigurationOSN(
                    topology = record.get(ExecutionsParser.topologyColumn),
                    agents = record.get(ExecutionsParser.agentsColumn),
                    influencers = record.get(ExecutionsParser.influencersColumn),
                    bots = record.get(ExecutionsParser.botsColumn),
                    workWithTimeDynamics = record.get(ExecutionsParser.workWithTimeDynamicsColumn),
                    timeAccessForCommonUsers = record.get(ExecutionsParser.timeAccessForUsers),
                    timeAccessForBots = record.get(ExecutionsParser.timeAccessForBots),
                    initialNodesBarabasi = record.get(ExecutionsParser.initialNodesBarabasiColumn),
                    nodesInBarabasi = record.get(ExecutionsParser.nodeEdgesBarabasiColumn),
                    numberOfInitialBelievers = record.get(ExecutionsParser.initialBelieversColumn),
                    vulnerabilityMean = record.get(ExecutionsParser.vulnerabilityMeanColumn),
                    recoveryMean = record.get(ExecutionsParser.recoveryMeanColumn),
                    sharingMean = record.get(ExecutionsParser.sharingMeanCloumn),
                    numberOfTicks = record.get(ExecutionsParser.numberSteps),
                    sharingDebunking = record.get(ExecutionsParser.sharingDebunking),
                    ticksToStartLosingInterest = record.get(ExecutionsParser.ticksLostInterest),
                    seed = record.get(ExecutionsParser.randomSeedColumn)
                )
            )
            keyMapForConfigIds[record.get(ExecutionsParser.configIdColumn)] = configId
        }

        for (record in parser.parserForSteps) {
            executionsResultsRepository.insertStepOSN(
                Step(
                    record.get(ExecutionsParser.tickColumn),
                    record.get(ExecutionsParser.believersColumn),
                    record.get(ExecutionsParser.factCheckers),
                    keyMapForConfigIds[record.get(ExecutionsParser.configIdColumn)].toString(),
                    record.get(ExecutionsParser.believersSharingColumn)
                )
            )
        }

        println("Starting to calculate error for datasets not labeled. Total of ${keyMapForConfigIds.values.size} different configurations inserted")

        for (value in keyMapForConfigIds.values) {
            val listNotLabeled = datasetNotLabeledRepository.getDataSets()
            val listLabeled = datasetLabeledRepository.getDatasets()
            val config = executionsResultsRepository.getConfigurationOSN(value)
            for (dataset in listNotLabeled) {
                executionsResultsRepository.insertErrorNotLabeled(calculateNrmseNotLabeled(config, dataset))
            }

            println("Calculated error not labeled")

            for (dataset in listLabeled) {
                executionsResultsRepository.insertErrorLabeled(calculateNrmseLabeled(config, dataset))
            }

            println("Calculated error labeled")
        }

    }

    private fun calculateNrmseLabeledSocial(
        config: ConfigurationSocialFakeNews,
        dataset: DatasetLabeled
    ): ErrorSocialForLabeled {
        var i = 0
        var j = 0
        var numberOfBelieversInDay: Double
        var percentageOfBelieversInStep: Double
        var percentageOfBelieversInDataset: Double
        var numberOfDeniersInDay: Double
        var percentageOfDeniersInStep: Double
        var percentageOfDeniersInDataset: Double

        var totalSumBelievers = 0.0
        var totalSumDeniers = 0.0

        var PmaxBelievers = 0.0
        var PminBelievers = 0.0
        var PmaxDeniers = 0.0
        var PminDeniers = 0.0
        while (i < 15) {
            numberOfBelieversInDay = datasetLabeledRepository.getNumberOfBelieversForDay(dataset.id!!, i).toDouble()
            percentageOfBelieversInDataset = (numberOfBelieversInDay * 100) / datasetLabeledRepository.getTotalUsers(dataset.id)
            percentageOfBelieversInStep = (executionsResultsRepository.getNumberOfBelieversInDaySocial(config.id.toString(), j).toDouble() * 100) / executionsResultsRepository.getTotalUsersSocial(config.id.toString())

            numberOfDeniersInDay = datasetLabeledRepository.getNumberOfDeniersForDay(dataset.id, i).toDouble()
            percentageOfDeniersInDataset = (numberOfDeniersInDay * 100) / datasetLabeledRepository.getTotalUsers(dataset.id)
            percentageOfDeniersInStep = (executionsResultsRepository.getNumberOfDeniersInDaySocial(config.id.toString(), j).toDouble() * 100) /executionsResultsRepository.getTotalUsersSocial(config.id.toString())

            if (i == 0) {
                PminBelievers = percentageOfBelieversInDataset
                PminDeniers = percentageOfDeniersInDataset
            }

            totalSumBelievers += Math.pow((percentageOfBelieversInDataset - percentageOfBelieversInStep), 2.0)
            totalSumDeniers += Math.pow((percentageOfDeniersInDataset - percentageOfDeniersInStep), 2.0)

            if (PmaxBelievers < percentageOfBelieversInDataset) {
                PmaxBelievers = percentageOfBelieversInDataset
            }

            if (PmaxDeniers < percentageOfDeniersInDataset) {
                PmaxDeniers = percentageOfDeniersInDataset
            }

            if (PminBelievers > percentageOfBelieversInDataset) {
                PminBelievers = percentageOfBelieversInDataset
            }

            if (PminDeniers > percentageOfDeniersInDataset) {
                PminDeniers = percentageOfDeniersInDataset
            }

            i++
            j += executionsResultsRepository.normalizedAddedValueForLabeledSocial(config.id.toString())

        }

        val rmseBelievers = Math.sqrt(totalSumBelievers / 15)
        val rmseDeniers = Math.sqrt(totalSumDeniers / 15)
        val rmseTotal = Math.sqrt(rmseBelievers + rmseDeniers)
        val nrmseBelievers = rmseBelievers / (PmaxBelievers - PminBelievers)
        val nrmseDenying = rmseDeniers / (PmaxDeniers - PminDeniers)

        return ErrorSocialForLabeled(
            configId = config.id,
            DataSetLabeledId = dataset.id,
            rmseBelievers = rmseBelievers,
            rmseDeniers = rmseDeniers,
            nmseBelievers = nrmseBelievers,
            nmseDeniers = nrmseDenying,
            rmseTotal = rmseTotal
        )


    }

    suspend fun parseExecutionResultsFilesFromSocialFakeNewsModel() = coroutineScope {
        this.launch {
            if (fileWithConfig == null ||
                fileWithSteps == null
            ) {
                println("All files for execution result parsing must be present")
                return@launch
            }

            val parser = executionsParserRepository.getParser(
                fileWithConfig = fileWithConfig!!,
                fileWithSteps = fileWithSteps!!
            )

            val keyMapForConfigIds = mutableMapOf<String, Int>()
            var configId: Int
            _parsingDatasetLabeledPercentage.emit(0)
            for (record in parser.parserForConfig) {
                configId = executionsResultsRepository.insertConfigurationSocial(
                    ConfigurationSocialFakeNews(

                        believersCount = record.get(ExecutionsParser.nBelieversColumn),
                        deniersCount = record.get(ExecutionsParser.denierCountColumn),
                        susceptibleCount = (Integer.parseInt(record.get(ExecutionsParser.nAgentsColumn)) -
                                (Integer.parseInt(record.get(ExecutionsParser.nBelieversColumn)) +
                                        Integer.parseInt(record.get(ExecutionsParser.denierCountColumn))))
                            .toString(),
                        averageFollowers = record.get(ExecutionsParser.avgFollowersColumn),
                        selectedTopology = record.get(ExecutionsParser.selectedTopologyColumn),
                        numberOfTicks = record.get(ExecutionsParser.nTicksColumn),
                        totalAgents = record.get(ExecutionsParser.nAgentsColumn),
                        nodesInBarabasi = record.get(ExecutionsParser.nodesInBarabasiColumn),
                        initialNodesInBarabasi = record.get(ExecutionsParser.initialNodesBarabasiColumn),
                        nBots = record.get(ExecutionsParser.nBotsColumn),
                        createInterest = record.get(ExecutionsParser.createInterestsColumn),
                        nOfInterests = record.get(ExecutionsParser.nOfInterestsColumn),
                        nOfInfluencersBelievers = record.get(ExecutionsParser.nInfluencersBelieversColumn),
                        nOfInfluencersDeniers = record.get(ExecutionsParser.nInfluencersDeniersColumn),
                        nOfInfluencersSusceptibles = record.get(ExecutionsParser.nInfluencersSusceptiblesColumn),
                        nFollowersToBeInfluencer = record.get(ExecutionsParser.nFollowersInfluencersColumn),
                        nBotsConnections = record.get(ExecutionsParser.nBotsConnectionsColumn),
                        pInfl = record.get(ExecutionsParser.influencersProbColumn),
                        pbelieve = record.get(ExecutionsParser.believeProbColumn),
                        pDeny = record.get(ExecutionsParser.denyProbSocialColumn),
                        pVacc = record.get(ExecutionsParser.vaccProbColumn),
                        seed = record.get(ExecutionsParser.seedColumn)
                    )
                )
                keyMapForConfigIds[record.get(ExecutionsParser.configSocialIdColumn)] = configId
            }
            _parsingDatasetLabeledPercentage.emit(25)
            for (record in parser.parserForSteps) {
                executionsResultsRepository.insertStepSocial(
                    Step(
                        record.get(ExecutionsParser.tickColumn),
                        record.get(ExecutionsParser.believersSocialColumn),
                        record.get(ExecutionsParser.deniersSocialColumn),
                        keyMapForConfigIds[record.get(ExecutionsParser.configSocialIdColumn)].toString(),
                        "0"
                    )
                )
            }
            _parsingDatasetLabeledPercentage.emit(50)
            println("Starting to calculate error for datasets labeled. Total of ${keyMapForConfigIds.keys.size} different configurations inserted")
            for (key in keyMapForConfigIds.keys) {
                val executionId = keyMapForConfigIds[key]
                executionId?.let {exeId ->
                    val config = executionsResultsRepository.getConfigurationSocial(executionId)

                    val listLabeled = datasetLabeledRepository.getDatasets()

                    for (dataset in listLabeled) {
                        executionsResultsRepository.insertErrorSocialLabeled(calculateNrmseLabeledSocial(config, dataset))
                    }

                    println("Calculated error labeled")
                }

            }
            _parsingDatasetLabeledPercentage.emit(100)
        }

    }
}