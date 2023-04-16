package ui.handler

import data.database.model.*
import data.parser.DatasetLabeledParser
import data.parser.DatasetNotLabeledParser
import data.parser.ExecutionsParser
import data.parser.model.TuitNewsRecord
import data.repository.*
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

    fun parseExecutionResultsFiles() {
        if (fileWithConfig == null ||
            fileWithSteps == null) {
            println("All files for execution result parsing must be present")
            return
        }

        val parser = executionsParserRepository.getParser(fileWithConfig = fileWithConfig!!, fileWithSteps = fileWithSteps!!)

        val keyMapForConfigIds = mutableMapOf<String, Int>()
        var configId: Int
        for (record in parser.parserForConfig) {
            configId = executionsResultsRepository.insertConfiguration(
                Configuration(
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
                    ticksToStartLosingInterest = record.get(ExecutionsParser.ticksLostInterest)
                )
            )
            keyMapForConfigIds[record.get(ExecutionsParser.configIdColumn)] = configId
        }

        for (record in parser.parserForSteps) {
            executionsResultsRepository.insertStep(
                Step (
                  record.get(ExecutionsParser.tickColumn),
                    record.get(ExecutionsParser.believersColumn),
                    record.get(ExecutionsParser.factCheckers),
                    keyMapForConfigIds[record.get(ExecutionsParser.configIdColumn)].toString(),
                    record.get(ExecutionsParser.believersSharingColumn)
                )
            )
        }
    }
}