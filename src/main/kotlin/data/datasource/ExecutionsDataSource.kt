package data.datasource

import data.database.FakeNewsDataBase
import data.database.model.Configuration
import data.database.model.ErrorForLabeled
import data.database.model.ErrorForNotLabeled
import data.database.model.Step
import org.apache.commons.csv.CSVParser
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

class ExecutionsDataSource : KoinComponent {

    private val db: FakeNewsDataBase by inject()

    fun insertConfiguration(config: Configuration): Int {
        val statement = db.createStatement()

        val insertQuery = String.format(
            insertConfigTemplate,
            config.topology,
            config.agents,
            config.influencers,
            config.bots,
            config.workWithTimeDynamics,
            config.timeAccessForCommonUsers,
            config.timeAccessForBots,
            config.initialNodesBarabasi,
            config.nodesInBarabasi,
            config.numberOfInitialBelievers,
            config.vulnerabilityMean,
            config.recoveryMean,
            config.sharingMean,
            config.numberOfTicks,
            config.sharingDebunking,
            config.ticksToStartLosingInterest
        )

        val getIdQuery = String.format(
            getConfigIdTemplate,
            config.topology,
            config.agents,
            config.influencers,
            config.bots,
            config.workWithTimeDynamics,
            config.timeAccessForCommonUsers,
            config.timeAccessForBots,
            config.initialNodesBarabasi,
            config.nodesInBarabasi,
            config.numberOfInitialBelievers,
            config.vulnerabilityMean,
            config.recoveryMean,
            config.sharingMean,
            config.numberOfTicks,
            config.sharingDebunking,
            config.ticksToStartLosingInterest
        )
        try {
            statement.execute(insertQuery)
            val result = statement.executeQuery(getIdQuery)
            if (result.next()) {
                return result.getInt(idConfigurationColumn)
            }
        } catch (e: SQLException) {
            db.logQueryError(insertQuery, e)
        }

        return -1
    }

    fun insertStep(step: Step) {
        val statement = db.createStatement()
        val insertQuery = String.format(
            insertResultStepTemplate,
            step.tick,
            step.believers,
            step.factCheckers,
            step.configId,
            step.believersSharing
        )

        try {
            statement.execute(insertQuery)
        } catch (e: SQLException) {
            db.logQueryError(insertQuery, e)
        }

    }

    fun insertExecutionsParametersFromFile(parser: CSVParser, keyMap: MutableMap<String, Int>) {
        val statement = db.createStatement()
        var insertQuery: String
        var getIdQuery: String
        for (csvRecord in parser) {
            insertQuery = String.format(
                insertConfigTemplate,
                csvRecord.get("topology"),
                csvRecord.get("agents"),
                csvRecord.get("influencers"),
                csvRecord.get("bots"),
                csvRecord.get("work_with_time_dynamics"),
                csvRecord.get("time_access_for_users"),
                csvRecord.get("time_access_for_bots"),
                csvRecord.get("initial_nodes_in_barabasi"),
                csvRecord.get("node_edges_in_barabasi"),
                csvRecord.get("initial_believers"),
                csvRecord.get("vulnerability_mean"),
                csvRecord.get("vulnerability_variance"),
                csvRecord.get("recovery_mean"),
                csvRecord.get("recovery_variance"),
                csvRecord.get("sharing_mean"),
                csvRecord.get("sharing_variance")
            )

            getIdQuery = String.format(
                getconfigIdTemplate,
                csvRecord.get("topology"),
                csvRecord.get("agents"),
                csvRecord.get("influencers"),
                csvRecord.get("bots"),
                csvRecord.get("work_with_time_dynamics"),
                csvRecord.get("time_access_for_users"),
                csvRecord.get("time_access_for_bots"),
                csvRecord.get("initial_nodes_in_barabasi"),
                csvRecord.get("node_edges_in_barabasi"),
                csvRecord.get("initial_believers"),
                csvRecord.get("vulnerability_mean"),
                csvRecord.get("vulnerability_variance"),
                csvRecord.get("recovery_mean"),
                csvRecord.get("recovery_variance"),
                csvRecord.get("sharing_mean"),
                csvRecord.get("sharing_variance")
            )
            try {
                statement.execute(insertQuery)
                try {
                    val resultQuery = statement.executeQuery(getconfigIdTemplate)
                    if (resultQuery.next()) {
                        keyMap[csvRecord.get("run")] = resultQuery.getInt("idConfiguration")
                    }
                } catch (e: SQLException) {
                    db.logQueryError(getIdQuery, e)
                }
            } catch (e: SQLException) {
                db.logQueryError(insertQuery, e)
            }

        }
    }

    fun insertExecutionStepsFromFile(parser: CSVParser, keyMap: MutableMap<String, Int>) {

        val statement = db.createStatement()

        var i = 0;
        var stepAddBelievers = 0
        var stepAddFactCheckers = 0
        var stepAddBelieversSharing = 0
        var ticksCsv: String
        var believersCsv: String
        var factCheckersCsv: String
        var runCsv: String
        var believersSharingCsv: String
        for (csvRecord in parser) {
            ticksCsv = csvRecord.get("tick")
            believersCsv = csvRecord.get("believers")
            factCheckersCsv = csvRecord.get("factCheckers")
            runCsv = csvRecord.get("run")
            believersSharingCsv = csvRecord.get("believersSharing")
            var insertSept = String.format(
                insertResultStepTemplate,
                ticksCsv,
                believersCsv,
                factCheckersCsv,
                keyMap[runCsv],
                believersSharingCsv
            )

                try {
                    statement.execute(insertSept)
                    // TODO: calculateError for labeled
                    // TODO: calculateError for not labeled
                } catch (e: SQLException) {
                    db.logQueryError(insertSept, e)
                }


//            if (i != 0 && i.mod(3) == 0) {
//                // TODO: calculate step error
//                stepAddBelievers += Integer.parseInt(believersCsv)
//                stepAddFactCheckers += Integer.parseInt(factCheckersCsv)
//                stepAddBelieversSharing += Integer.parseInt(believersSharingCsv)
//
//                Data
//            } else {
//                // Add beleivers and fact_checkers for error
//            }
        }


    }

    fun insertErrorForLabeled(error: ErrorForLabeled) {
        val statement = db.createStatement()
        val query = String.format(
            Locale.US,
            insertErrorForLabeled,
            error.configId,
            error.DataSetLabeledId,
            error.rmseBelievers,
            error.nmseBelievers,
            error.rmseDeniers,
            error.nmseDeniers,
            error.rmseTotal
        )

        try {
            statement.execute(query)
        } catch (e: SQLException) {
            db.logQueryError(query, e)
        }

    }

    fun insertErrorNotLabeled(error: ErrorForNotLabeled) {
        val statement = db.createStatement()
        val query = String.format(
            Locale.US,
            insertErrorForNotLabeled,
            error.configurationId,
            error.datasetNotLabeledId,
            error.rmse,
            error.nrmse
        )

        try {
            statement.execute(query)
        } catch (e: SQLException) {
            db.logQueryError(query, e)
        }
    }



    fun getListOfExecutions(): List<Configuration> {
        val result = mutableListOf<Configuration>()
        val statement = db.createStatement()

        val queryResult = statement.executeQuery(getListOfExecutionsQuery)
        while (queryResult.next()) {
            result.add(
                Configuration(
                    //id = queryResult.getInt(idConfigurationColumn),
                    topology = queryResult.getString(topologyConfigColumn),
                    agents = queryResult.getInt(agentsConfigColumn).toString(),
                    influencers = queryResult.getInt(influencersConfigColumn).toString(),
                    bots = queryResult.getInt(botsConfigColumn).toString(),
                    workWithTimeDynamics = queryResult.getBoolean(workWithTimeDynamicsConfigColumn).toString(),
                    timeAccessForCommonUsers = queryResult.getInt(timeAccessForCommonUsersConfigColumn).toString(),
                    timeAccessForBots = queryResult.getInt(timeAccessForBotsConfigColumn).toString(),
                    initialNodesBarabasi = queryResult.getInt(initialNodesInBarabasiConfigColumn).toString(),
                    nodesInBarabasi = queryResult.getInt(numberOfInitialBelieversConfigColumn).toString(),
                    numberOfInitialBelievers = queryResult.getInt(numberOfInitialBelieversConfigColumn).toString(),
                    vulnerabilityMean = queryResult.getDouble(vulnerabilityMeanConfigColumn).toString(),
                    recoveryMean = queryResult.getDouble(recoveryMeanConfigColumn).toString(),
                    sharingMean = queryResult.getDouble(sharingMeanConfigColumn).toString(),
                    numberOfTicks = queryResult.getInt(numberOfTicksConfigColumn).toString(),
                    sharingDebunking = queryResult.getDouble(SharingDebunkingConfigColumn).toString(),
                    ticksToStartLosingInterest = queryResult.getInt(TicksToStartLosingInterestConfigColumn).toString()
                )
            )
        }


        return result
    }

    fun getConfigurations(): List<Configuration> {

        val statement = db.createStatement()

        val resultList = mutableListOf<Configuration>()
        try {
            val queryResult = statement.executeQuery(getConfigs)

            while (queryResult.next()) {
                resultList.add(
                    Configuration(
                        topology = queryResult.getString(topologyConfigColumn),
                        agents = queryResult.getInt(agentsConfigColumn).toString(),
                        influencers = queryResult.getInt(influencersConfigColumn).toString(),
                        bots = queryResult.getInt(botsConfigColumn).toString(),
                        workWithTimeDynamics = queryResult.getBoolean(workWithTimeDynamicsConfigColumn).toString(),
                        timeAccessForCommonUsers = queryResult.getInt(timeAccessForCommonUsersConfigColumn).toString(),
                        timeAccessForBots = queryResult.getInt(timeAccessForBotsConfigColumn).toString(),
                        initialNodesBarabasi = queryResult.getInt(initialNodesInBarabasiConfigColumn).toString(),
                        nodesInBarabasi = queryResult.getInt(nodesInBarabasiConfigColumn).toString(),
                        numberOfInitialBelievers = queryResult.getInt(numberOfInitialBelieversConfigColumn).toString(),
                        vulnerabilityMean = queryResult.getDouble(vulnerabilityMeanConfigColumn).toString(),
                        recoveryMean = queryResult.getDouble(recoveryMeanConfigColumn).toString(),
                        sharingMean = queryResult.getDouble(sharingMeanConfigColumn).toString(),
                        numberOfTicks = queryResult.getInt(numberOfTicksConfigColumn).toString(),
                        sharingDebunking = queryResult.getDouble(SharingDebunkingConfigColumn).toString(),
                        ticksToStartLosingInterest = queryResult.getInt(TicksToStartLosingInterestConfigColumn).toString()
                    ).apply {
                        id = queryResult.getInt(idConfigurationColumn)
                    }
                )

            }

        } catch (e: SQLException) {
            db.logQueryError(getConfigIds, e)
        }


        return resultList
    }

    fun getTotalUsersForconfig(configId: String): Int? {
        val statement = db.createStatement()
        val getQuery = String.format(
            getNumberOfUserInConfigTemplate,
            configId
        )
        try {
            val resultQuery = statement.executeQuery(getQuery)
            if (resultQuery.next()) {
                return resultQuery.getInt(agentsConfigColumn)
            }
        } catch (e: SQLException) {
            db.logQueryError(getQuery, e)
        }

        return null
    }

    fun getMaxNumberOfBelieversSharingInStepNormalized(configId: String, tick: Int, stepsToNormalize: Int): Int {
        var getQuery: String
        var result = 0
        var tmpToCheckResult: Int
        val statement = db.createStatement()
        var queryResult: ResultSet
        for (i in tick..tick + stepsToNormalize) {
            getQuery = String.format(
                getBelieversSharingPerTickTemplate,
                configId,
                i
            )
            try {

                queryResult = statement.executeQuery(getQuery)
                if (queryResult.next()) {
                    tmpToCheckResult = queryResult.getInt(believersSharingColumn)
                    result = if (tmpToCheckResult > result) {
                        tmpToCheckResult
                    } else {
                        result
                    }
                }

            } catch (e: SQLException) {
                db.logQueryError(getQuery, e)
            }
        }

        return result
    }

    fun getMaxNumberOfBelieversInStepNormalized(configId: String, tick: Int, stepsToNormalize: Int): Int {
        var getQuery: String
        var result = 0
        var tmpToCheckResult: Int
        val statement = db.createStatement()
        var queryResult: ResultSet
        for (i in tick..tick + stepsToNormalize) {
            getQuery = String.format(
                getBelieversPerTickTemplate,
                configId,
                i
            )
            try {

                queryResult = statement.executeQuery(getQuery)
                if (queryResult.next()) {
                    tmpToCheckResult = queryResult.getInt(believersStepColumn)
                    result = if (tmpToCheckResult > result) {
                        tmpToCheckResult
                    } else {
                        result
                    }
                }

            } catch (e: SQLException) {
                db.logQueryError(getQuery, e)
            }
        }

        return result
    }

    fun getMaxNumberOfDeniersInStepNormalized(configId: String, tick: Int, stepsToNormalize: Int): Int {
        var getQuery: String
        var result = 0
        var tmpToCheckResult: Int
        val statement = db.createStatement()
        var queryResult: ResultSet
        for (i in tick..tick + stepsToNormalize) {
            getQuery = String.format(
                getDeniersPerTickTemplate,
                configId,
                i
            )
            try {

                queryResult = statement.executeQuery(getQuery)
                if (queryResult.next()) {
                    tmpToCheckResult = queryResult.getInt(factCheckersStepColumn)
                    result = if (tmpToCheckResult > result) {
                        tmpToCheckResult
                    } else {
                        result
                    }
                }

            } catch (e: SQLException) {
                db.logQueryError(getQuery, e)
            }
        }

        return result
    }

    companion object {

        private const val configurationTableName = "Configuration"
        private const val idConfigurationColumn = "idConfiguration"
        private const val topologyConfigColumn = "topology"
        private const val agentsConfigColumn = "agents"
        private const val influencersConfigColumn = "influencers"
        private const val botsConfigColumn = "Bots"
        private const val workWithTimeDynamicsConfigColumn = "WorkWithTimeDynamics"
        private const val timeAccessForCommonUsersConfigColumn = "TimeAccessForCommonUsers"
        private const val timeAccessForBotsConfigColumn = "TimeAccessForBots"
        private const val initialNodesInBarabasiConfigColumn = "InitialNodesInBarabasi"
        private const val nodesInBarabasiConfigColumn = "NodesInbarabasi"
        private const val numberOfInitialBelieversConfigColumn = "NumberOfInitialBeleivers"
        private const val vulnerabilityMeanConfigColumn = "vulnerabilityMean"
        private const val recoveryMeanConfigColumn = "RecoveryMean"
        private const val sharingMeanConfigColumn = "SharingMean"
        private const val numberOfTicksConfigColumn = "numberOfTicks"
        private const val SharingDebunkingConfigColumn = "SharingDebunking"
        private const val TicksToStartLosingInterestConfigColumn = "TicksToStartLosingInterest"

        private const val stepTableName = "Step"
        private const val idStepColumn = "idStep"
        private const val tickStepColumn = "tick"
        private const val believersStepColumn = "believers"
        private const val factCheckersStepColumn = "fact_checkers"
        private const val idConfigStepColumn = "idConfig"
        private const val believersSharingColumn = "beleiversSharing"

        private const val errorForLabeledTable = "ErrorForLabeled"
        private const val idErrorForLabeledColumn = "idErrorForLabeled"
        private const val configurationIdColumn = "ConfigurationId"
        private const val dataSetLabeledIdColumn = "DataSetLabeledId"
        private const val rmseBelieversColumn = "rmseBelievers"
        private const val nrmseBelieversColumn = "nrmseBelievers"
        private const val rmseDeniersColumn = "rmseDeniers"
        private const val nrmseDeniersColumn = "nrmseDeniers"
        private const val rmseTotalColumn = "rmseTotal"

        private const val errorForNotLabeledTable = "ErrorForNotLabeled"
        private const val idErrorForNotLabeledColumn = "idErrorForNotLabeled"
        private const val configurationIdErrorForNotLabeledColumn = "ConfigurationId"
        private const val datasetNotLabeledIdErrorForNotLabeledColumn = "DataSetNotLabeledId"
        private const val rmseErrorNotLabeledColumn = "rmse"
        private const val nrmseErrorNotLabeledColumn = "nrmse"

        private const val insertConfigTemplate = ("INSERT INTO $configurationTableName "
                + "($topologyConfigColumn, $agentsConfigColumn, $influencersConfigColumn, $botsConfigColumn, "
                + "$workWithTimeDynamicsConfigColumn, $timeAccessForCommonUsersConfigColumn, "
                + "$timeAccessForBotsConfigColumn, $initialNodesInBarabasiConfigColumn, "
                + "$nodesInBarabasiConfigColumn, $numberOfInitialBelieversConfigColumn, "
                + "$vulnerabilityMeanConfigColumn, $recoveryMeanConfigColumn, "
                + "$sharingMeanConfigColumn, $numberOfTicksConfigColumn, $SharingDebunkingConfigColumn, "
                + "$TicksToStartLosingInterestConfigColumn) "
                + "VALUES "
                + "(\"%s\", %s, %s, %s, %s, %s, "
                + "%s, %s, %s, %s, "
                + "%s, %s, %s, %s, %s, %s)")

        private const val getConfigIdTemplate = ("SELECT $idConfigurationColumn FROM $configurationTableName WHERE "
                + "$topologyConfigColumn = \"%s\" AND "
                + "$agentsConfigColumn = %s AND "
                + "$influencersConfigColumn = %s AND "
                + "$botsConfigColumn = %s AND "
                + "$workWithTimeDynamicsConfigColumn = %s AND "
                + "$timeAccessForCommonUsersConfigColumn = %s AND "
                + "$timeAccessForBotsConfigColumn = %s AND "
                + "$initialNodesInBarabasiConfigColumn = %s AND "
                + "$nodesInBarabasiConfigColumn = %s AND "
                + "$numberOfInitialBelieversConfigColumn = %s AND "
                + "$vulnerabilityMeanConfigColumn = %s AND "
                + "$recoveryMeanConfigColumn = %s AND "
                + "$sharingMeanConfigColumn = %s AND "
                + "$numberOfTicksConfigColumn = %s AND "
                + "$SharingDebunkingConfigColumn = %s AND "
                + "$TicksToStartLosingInterestConfigColumn = %s")

        private const val getconfigIdTemplate =
            "SELECT $idConfigurationColumn FROM $configurationTableName WHERE $idConfigurationColumn = (SELECT MAX($idConfigurationColumn) FROM $configurationTableName)"

        private const val getConfigIds = ("SELECT $idConfigurationColumn FROM $configurationTableName")

        private const val getConfigs = "SELECT * FROM $configurationTableName"

        private const val insertResultStepTemplate = ("INSERT INTO $stepTableName " +
                "($tickStepColumn, $believersStepColumn, $factCheckersStepColumn, $idConfigStepColumn, $believersSharingColumn) VALUES " +
                "(%s, %s, %s, %s, %s)")

        private const val getNumberOfUserInConfigTemplate =
            ("SELECT $agentsConfigColumn FROM $configurationTableName WHERE $idConfigurationColumn = %s")

        private const val getBelieversSharingPerTickTemplate =
            ("SELECT $believersSharingColumn FROM $stepTableName WHERE $idConfigStepColumn = %s AND $tickStepColumn = %d")

        private const val getBelieversPerTickTemplate =
            "SELECT $believersStepColumn FROM $stepTableName WHERE $idConfigStepColumn = %s AND $tickStepColumn = %d"

        private const val getDeniersPerTickTemplate =
            "SELECT $factCheckersStepColumn FROM $stepTableName WHERE $idConfigStepColumn = %s AND $tickStepColumn = %d"

        private const val getListOfExecutionsQuery = "SELECT * FROM $configurationTableName"

        private const val insertErrorForLabeled = "INSERT INTO $errorForLabeledTable " +
                "($configurationIdColumn, $dataSetLabeledIdColumn, $rmseBelieversColumn, " +
                "$nrmseBelieversColumn, $rmseDeniersColumn, $nrmseDeniersColumn, $rmseTotalColumn) VALUES " +
                "(%d, %d, %,.4f, %,.4f, %,.4f, %,.4f, %,.4f)"

        private const val insertErrorForNotLabeled = "INSERT INTO $errorForNotLabeledTable " +
                "($configurationIdErrorForNotLabeledColumn, $datasetNotLabeledIdErrorForNotLabeledColumn, " +
                "$rmseErrorNotLabeledColumn, $nrmseErrorNotLabeledColumn) VALUES " +
                "(%d, %d, %,.4f, %.4f)"

    }
}