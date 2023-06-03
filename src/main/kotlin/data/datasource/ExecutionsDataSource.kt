package data.datasource

import data.database.FakeNewsDataBase
import data.database.model.*
import org.apache.commons.csv.CSVParser
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

class ExecutionsDataSource : KoinComponent {

    private val db: FakeNewsDataBase by inject()

    fun insertConfigurationOSN(config: ConfigurationOSN): Int {
        val statement = db.createStatement()

        val insertQuery = String.format(
            insertConfigOSNTemplate,
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
            config.ticksToStartLosingInterest,
            config.seed
        )

        val getIdQuery = String.format(
            getConfigOSNIdTemplate,
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

    fun insertConfigurationSocialFakeNews(config: ConfigurationSocialFakeNews): Int {
        val statement = db.createStatement()

        val insertQuery = String.format(
            insertConfigSocialTemplate,
            config.believersCount,
            config.deniersCount,
            config.susceptibleCount,
            config.averageFollowers,
            config.selectedTopology,
            config.numberOfTicks,
            config.totalAgents,
            config.nodesInBarabasi,
            config.initialNodesInBarabasi,
            config.nBots,
            config.createInterest,
            config.nOfInterests,
            config.nOfInfluencersBelievers,
            config.nOfInfluencersDeniers,
            config.nOfInfluencersSusceptibles,
            config.nFollowersToBeInfluencer,
            config.nBotsConnections,
            config.pInfl,
            config.pbelieve,
            config.pDeny,
            config.pVacc,
            config.seed
        )

        val getIdQuery = String.format(
            getConfigSocialIdTemplate,
            config.believersCount,
            config.deniersCount,
            config.susceptibleCount,
            config.averageFollowers,
            config.selectedTopology,
            config.numberOfTicks,
            config.totalAgents,
            config.nodesInBarabasi,
            config.initialNodesInBarabasi,
            config.nBots,
            config.createInterest,
            config.nOfInterests,
            config.nOfInfluencersBelievers,
            config.nOfInfluencersDeniers,
            config.nOfInfluencersSusceptibles,
            config.nFollowersToBeInfluencer,
            config.nBotsConnections,
            config.pInfl,
            config.pbelieve,
            config.pDeny,
            config.pVacc
        )
        try {
            statement.execute(insertQuery)
            val result = statement.executeQuery(getIdQuery)
            if (result.next()) {
                return result.getInt(idConfigurationSocialColumn)
            }
        } catch (e: SQLException) {
            db.logQueryError(insertQuery, e)
        }

        return -1
    }

    fun insertStepOSN(step: Step) {
        val statement = db.createStatement()
        val insertQuery = String.format(
            insertResultStepOSNTemplate,
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

    fun insertStepSocialFakeNews(step: Step) {
        val statement = db.createStatement()
        val insertQuery = String.format(
            insertResultStepSocialTemplate,
            step.tick,
            step.believers,
            step.factCheckers,
            step.configId
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
                insertConfigOSNTemplate,
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
                csvRecord.get("sharing_variance"),
                csvRecord.get("randomSeed")
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
                insertResultStepOSNTemplate,
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

    fun insertErrorSocialForLabeled(error: ErrorSocialForLabeled) {
        val statement = db.createStatement()
        val query = String.format(
            Locale.US,
            insertSocialErrorForLabeled,
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


    fun getListOfExecutions(): List<ConfigurationOSN> {
        val result = mutableListOf<ConfigurationOSN>()
        val statement = db.createStatement()

        val queryResult = statement.executeQuery(getListOfExecutionsQuery)
        while (queryResult.next()) {
            result.add(
                ConfigurationOSN(
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
                    ticksToStartLosingInterest = queryResult.getInt(TicksToStartLosingInterestConfigColumn).toString(),
                    seed = queryResult.getString(seedOSNConfigColumn).toString()
                )
            )
        }


        return result
    }

    fun getConfigurations(): List<ConfigurationOSN> {

        val statement = db.createStatement()

        val resultList = mutableListOf<ConfigurationOSN>()
        try {
            val queryResult = statement.executeQuery(getConfigs)

            while (queryResult.next()) {
                resultList.add(
                    ConfigurationOSN(
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
                        ticksToStartLosingInterest = queryResult.getInt(TicksToStartLosingInterestConfigColumn)
                            .toString(),
                        seed = queryResult.getString(seedOSNConfigColumn)
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

    fun getBestOSNConfigurationsForDataSetNotLabeled(datasetId: Int): List<ConfigurationOSN> {
        val statement = db.createStatement()

        val resultList = mutableListOf<ConfigurationOSN>()
        val query = String.format(getBest10Configs, datasetId.toString())
        try {
            val queryResult = statement.executeQuery(query)

            while (queryResult.next()) {
                resultList.add(
                    ConfigurationOSN(
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
                        ticksToStartLosingInterest = queryResult.getInt(TicksToStartLosingInterestConfigColumn)
                            .toString(),
                        seed = queryResult.getString(seedOSNConfigColumn)
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

    fun getBestOSNConfigurationsForDataSetLabeled(datasetLabeledId: Int): List<ConfigurationOSN> {
        val statement = db.createStatement()

        val resultList = mutableListOf<ConfigurationOSN>()
        val query = String.format(getBest10ConfigsForOSNLabeled, datasetLabeledId.toString())
        try {
            val queryResult = statement.executeQuery(query)

            while (queryResult.next()) {
                resultList.add(
                    ConfigurationOSN(
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
                        ticksToStartLosingInterest = queryResult.getInt(TicksToStartLosingInterestConfigColumn)
                            .toString(),
                        seed = queryResult.getString(seedOSNConfigColumn)
                    ).apply {
                        id = queryResult.getInt(idConfigurationColumn)
                    }
                )

            }

        } catch (e: SQLException) {
            db.logQueryError(query, e)
        }


        return resultList
    }

    fun getBestSocialConfigurationForDataSetLabeled (datasetId: Int): List<ConfigurationSocialFakeNews> {
        val statement = db.createStatement()
        val query = String.format(getBestConfigSocial, datasetId.toString())
        val resultList = mutableListOf<ConfigurationSocialFakeNews>()
        try {
            val queryResult = statement.executeQuery(query)

            while (queryResult.next()) {
                resultList.add(
                    ConfigurationSocialFakeNews(
                        believersCount = queryResult.getString(believersCountSocialColumn),
                        deniersCount = queryResult.getString(deniersCountSocialColumn),
                        susceptibleCount = queryResult.getString(susceptibleCountSocialColumn),
                        averageFollowers = queryResult.getString(averageFollowersSocialColumn),
                        selectedTopology = queryResult.getString(selectedTopologySocialColumn),
                        numberOfTicks = queryResult.getString(numberOfTicksSocialColumn),
                        totalAgents = queryResult.getString(totalAgentsSocialColumn),
                        nodesInBarabasi = queryResult.getString(nodesInBarabasiSocialColumn),
                        initialNodesInBarabasi = queryResult.getString(initialNodesInBarabasiSocialColumn),
                        nBots = queryResult.getString(nBotsSocialColumn),
                        createInterest = queryResult.getString(createInterestSocialColumn),
                        nOfInterests = queryResult.getString(nOfInterestsSocialColumn),
                        nOfInfluencersBelievers = queryResult.getString(nOfInfluencersBelieversSocialColumn),
                        nOfInfluencersDeniers = queryResult.getString(nOfInfluencersDeniersSocialColumn),
                        nOfInfluencersSusceptibles = queryResult.getString(nOfInfluencersSusceptiblesSocialColumn),
                        nFollowersToBeInfluencer = queryResult.getString(nFollowersToBeInfluencerSocialColumn),
                        nBotsConnections = queryResult.getString(nBotsConnectionsSocialColumn),
                        pInfl = queryResult.getString(pInflSocialColumn),
                        pbelieve = queryResult.getString(pbelieveSocialColumn),
                        pDeny = queryResult.getString(pDenySocialColumn),
                        pVacc = queryResult.getString(pVaccSocialColumn),
                        seed = queryResult.getString(seedSocialColumn)
                    ).apply {
                        id = queryResult.getInt(idConfigurationSocialColumn)
                    }
                )

            }

        } catch (e: SQLException) {
            db.logQueryError(query, e)
        }


        return resultList
    }

    fun getConfigurationOSN(id: Int): ConfigurationOSN {
        val statement = db.createStatement()
        val query = String.format(getConfigOSN, id.toString())
        try {

            val queryResult = statement.executeQuery(query)

            while (queryResult.next()) {

                    ConfigurationOSN(
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
                        ticksToStartLosingInterest = queryResult.getInt(TicksToStartLosingInterestConfigColumn)
                            .toString(),
                        seed = queryResult.getString(seedOSNConfigColumn)
                    ).apply {
                        this.id = queryResult.getInt(idConfigurationColumn)
                    }


            }

        } catch (e: SQLException) {
            db.logQueryError(query, e)
        }


        return ConfigurationOSN().apply {
            this.id = id
        }
    }

    fun getConfigurationsSocial(): List<ConfigurationSocialFakeNews> {

        val statement = db.createStatement()

        val resultList = mutableListOf<ConfigurationSocialFakeNews>()
        try {
            val queryResult = statement.executeQuery(getConfigsSocial)

            while (queryResult.next()) {
                resultList.add(
                    ConfigurationSocialFakeNews(
                            believersCount = queryResult.getString(believersCountSocialColumn),
                            deniersCount = queryResult.getString(deniersCountSocialColumn),
                            susceptibleCount = queryResult.getString(susceptibleCountSocialColumn),
                            averageFollowers = queryResult.getString(averageFollowersSocialColumn),
                            selectedTopology = queryResult.getString(selectedTopologySocialColumn),
                            numberOfTicks = queryResult.getString(numberOfTicksSocialColumn),
                            totalAgents = queryResult.getString(totalAgentsSocialColumn),
                            nodesInBarabasi = queryResult.getString(nodesInBarabasiSocialColumn),
                            initialNodesInBarabasi = queryResult.getString(initialNodesInBarabasiSocialColumn),
                            nBots = queryResult.getString(nBotsSocialColumn),
                            createInterest = queryResult.getString(createInterestSocialColumn),
                            nOfInterests = queryResult.getString(nOfInterestsSocialColumn),
                            nOfInfluencersBelievers = queryResult.getString(nOfInfluencersBelieversSocialColumn),
                            nOfInfluencersDeniers = queryResult.getString(nOfInfluencersDeniersSocialColumn),
                            nOfInfluencersSusceptibles = queryResult.getString(nOfInfluencersSusceptiblesSocialColumn),
                            nFollowersToBeInfluencer = queryResult.getString(nFollowersToBeInfluencerSocialColumn),
                            nBotsConnections = queryResult.getString(nBotsConnectionsSocialColumn),
                            pInfl = queryResult.getString(pInflSocialColumn),
                            pbelieve = queryResult.getString(pbelieveSocialColumn),
                            pDeny = queryResult.getString(pDenySocialColumn),
                            pVacc = queryResult.getString(pVaccSocialColumn)
                    ).apply {
                        id = queryResult.getInt(idConfigurationSocialColumn)
                    }
                )

            }

        } catch (e: SQLException) {
            db.logQueryError(getConfigsSocial, e)
        }


        return resultList
    }

    fun getConfigurationSocial(configurationId: Int): ConfigurationSocialFakeNews {
        val statement = db.createStatement()
        val query = String.format(getConfigSocialTemplate, configurationId.toString())
        try {
            val queryResult = statement.executeQuery(query)
            if (queryResult.next()) {
                return ConfigurationSocialFakeNews(
                    believersCount = queryResult.getString(believersCountSocialColumn),
                    deniersCount = queryResult.getString(deniersCountSocialColumn),
                    susceptibleCount = queryResult.getString(susceptibleCountSocialColumn),
                    averageFollowers = queryResult.getString(averageFollowersSocialColumn),
                    selectedTopology = queryResult.getString(selectedTopologySocialColumn),
                    numberOfTicks = queryResult.getString(numberOfTicksSocialColumn),
                    totalAgents = queryResult.getString(totalAgentsSocialColumn),
                    nodesInBarabasi = queryResult.getString(nodesInBarabasiSocialColumn),
                    initialNodesInBarabasi = queryResult.getString(initialNodesInBarabasiSocialColumn),
                    nBots = queryResult.getString(nBotsSocialColumn),
                    createInterest = queryResult.getString(createInterestSocialColumn),
                    nOfInterests = queryResult.getString(nOfInterestsSocialColumn),
                    nOfInfluencersBelievers = queryResult.getString(nOfInfluencersBelieversSocialColumn),
                    nOfInfluencersDeniers = queryResult.getString(nOfInfluencersDeniersSocialColumn),
                    nOfInfluencersSusceptibles = queryResult.getString(nOfInfluencersSusceptiblesSocialColumn),
                    nFollowersToBeInfluencer = queryResult.getString(nFollowersToBeInfluencerSocialColumn),
                    nBotsConnections = queryResult.getString(nBotsConnectionsSocialColumn),
                    pInfl = queryResult.getString(pInflSocialColumn),
                    pbelieve = queryResult.getString(pbelieveSocialColumn),
                    pDeny = queryResult.getString(pDenySocialColumn),
                    pVacc = queryResult.getString(pVaccSocialColumn)
                ).apply {
                    id = queryResult.getInt(idConfigurationSocialColumn)
                }
            }
        } catch (e: SQLException) {
            db.logQueryError(query, e)
        }

        return ConfigurationSocialFakeNews().apply {
            id = configurationId
        }
    }

    fun getTotalUsersForConfig(configId: String): Int? {
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

    fun getTotalUsersSocialForConfig(configId: String): Int? {
        val statement = db.createStatement()
        val getQuery = String.format(
            getNumberOfUserInConfigSocialTemplate,
            configId
        )
        try {
            val resultQuery = statement.executeQuery(getQuery)
            if (resultQuery.next()) {
                return resultQuery.getInt(totalAgentsSocialColumn)
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
                    if (i == tick) {
                        result = tmpToCheckResult
                    }
                    result = if (tmpToCheckResult < result) {
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

    fun getMaxNumberOfBelieversInStepNormalizedSocial(configId: String, tick: Int, stepsToNormalize: Int): Int {
        var getQuery: String
        var result = 0
        var tmpToCheckResult: Int
        val statement = db.createStatement()
        var queryResult: ResultSet
        for (i in tick..tick + stepsToNormalize) {
            getQuery = String.format(
                getBelieversPerTickTemplateSocial,
                configId,
                i
            )
            try {
                queryResult = statement.executeQuery(getQuery)
                if (queryResult.next()) {
                    tmpToCheckResult = queryResult.getInt(believersStepSocialColumn)
                    if (i == tick) {
                        result = tmpToCheckResult
                    }
                    result = if (tmpToCheckResult < result) {
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
                    if (i == tick) {
                        result = tmpToCheckResult
                    }
                    result = if (tmpToCheckResult < result) {
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

    fun getMaxNumberOfDeniersInStepNormalizedSocial (configId: String, tick: Int, stepsToNormalize: Int): Int {
        var getQuery: String
        var result = 0
        var tmpToCheckResult: Int
        val statement = db.createStatement()
        var queryResult: ResultSet
        for (i in tick..tick + stepsToNormalize) {
            getQuery = String.format(
                getDeniersPerTickTemplateSocial,
                configId,
                i
            )
            try {

                queryResult = statement.executeQuery(getQuery)
                if (queryResult.next()) {
                    tmpToCheckResult = queryResult.getInt(deniersStepSocialColumn)
                    if (i == tick) {
                        result = tmpToCheckResult
                    }
                    result = if (tmpToCheckResult < result) {
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

    fun getNumberOfTicksSocial(configId: String): Int {
        val statement = db.createStatement()
        val query = String.format(
            getNumberOfTicksSocial,
            configId
        )
        try {
            val queryResult = statement.executeQuery(query)
            if (queryResult.next()) {
                return queryResult.getInt(numberOfTicksSocialColumn)
            }
        } catch (e: SQLException) {
            db.logQueryError(query, e)
        }
        return -1;
    }

    companion object {

        private const val configurationOSNTableName = "ConfigurationOSN"
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
        private const val seedOSNConfigColumn = "seed"

        private const val stepOSNTableName = "Step"
        private const val idStepColumn = "idStep"
        private const val tickStepColumn = "tick"
        private const val believersStepColumn = "believers"
        private const val factCheckersStepColumn = "fact_checkers"
        private const val idConfigStepColumn = "idConfig"
        private const val believersSharingColumn = "beleiversSharing"

        private const val errorForLabeledOSNTable = "ErrorForLabeledOSN"
        private const val idErrorForLabeledColumn = "idErrorForLabeled"
        private const val configurationIdColumn = "ConfigurationId"
        private const val dataSetLabeledIdColumn = "DataSetLabeledId"
        private const val rmseBelieversColumn = "rmseBelievers"
        private const val nrmseBelieversColumn = "nrmseBelievers"
        private const val rmseDeniersColumn = "rmseDeniers"
        private const val nrmseDeniersColumn = "nrmseDeniers"
        private const val rmseTotalColumn = "rmseTotal"

        private const val errorForLabeledSocialTable = "ErrorForLabeledSocialFakeNews"
        private const val idErrorForLabeledSocialFakeNewsColunm = "idErrorForLabeledSocialFakeNews"
        private const val ConfigurationIdSocialColumn = "ConfigurationId"
        private const val DataSetLabeledIdSocialColumn = "DataSetLabeledId"
        private const val rmseBelieversSocialColumn = "rmseBelievers"
        private const val nrmseBelieversSocialColumn = "nrmseBelievers"
        private const val rmseDeniersSocialColumn = "rmseDeniers"
        private const val nrmseDeniersSocialColumn = "nrmseDeniers"
        private const val rmseTotalSocialColumn = "rmseTotal"

        private const val errorForNotLabeledOSNTable = "ErrorForNotLabeledOSN"
        private const val idErrorForNotLabeledColumn = "idErrorForNotLabeled"
        private const val configurationIdErrorForNotLabeledColumn = "ConfigurationId"
        private const val datasetNotLabeledIdErrorForNotLabeledColumn = "DataSetNotLabeledId"
        private const val rmseErrorNotLabeledColumn = "rmse"
        private const val nrmseErrorNotLabeledColumn = "nrmse"

        private const val configurationSocialFakeNewsTableName = "ConfigurationSocialFakeNews"
        private const val idConfigurationSocialColumn = "idConfigurationSocialFakeNews"
        private const val believersCountSocialColumn = "believersCount"
        private const val deniersCountSocialColumn = "denyCount"
        private const val susceptibleCountSocialColumn = "susceptibleCount"
        private const val averageFollowersSocialColumn = "averageFollowers"
        private const val selectedTopologySocialColumn = "selectedTopology"
        private const val numberOfTicksSocialColumn = "numberOfTicks"
        private const val totalAgentsSocialColumn = "totalAgents"
        private const val nodesInBarabasiSocialColumn = "nodesInBarabasi"
        private const val initialNodesInBarabasiSocialColumn = "initialNodesInBarabasi"
        private const val nBotsSocialColumn = "nBots"
        private const val createInterestSocialColumn = "createInterest"
        private const val nOfInterestsSocialColumn = "nOfInterests"
        private const val nOfInfluencersBelieversSocialColumn = "nInfluencersBelievers"
        private const val nOfInfluencersDeniersSocialColumn = "nInfluencersDeniers"
        private const val nOfInfluencersSusceptiblesSocialColumn = "nInfluencersSusceptibles"
        private const val nFollowersToBeInfluencerSocialColumn = "nFollowersTobeInfluencer"
        private const val nBotsConnectionsSocialColumn = "nbotsConnections"
        private const val pInflSocialColumn = "pInfl"
        private const val pbelieveSocialColumn = "pbelieve"
        private const val pDenySocialColumn = "pDeny"
        private const val pVaccSocialColumn = "pVacc"
        private const val seedSocialColumn = "seed"

        private const val stepSocialTableName = "StepSocialFakeNews"
        private const val idStepSocialColumn = "idStepSocialFakeNews"
        private const val tickStepSocialColumn = "tick"
        private const val believersStepSocialColumn = "believers"
        private const val deniersStepSocialColumn = "deniers"
        private const val idConfigStepSocialColumn = "idConfig"

        private const val insertConfigOSNTemplate = ("INSERT INTO $configurationOSNTableName "
                + "($topologyConfigColumn, $agentsConfigColumn, $influencersConfigColumn, $botsConfigColumn, "
                + "$workWithTimeDynamicsConfigColumn, $timeAccessForCommonUsersConfigColumn, "
                + "$timeAccessForBotsConfigColumn, $initialNodesInBarabasiConfigColumn, "
                + "$nodesInBarabasiConfigColumn, $numberOfInitialBelieversConfigColumn, "
                + "$vulnerabilityMeanConfigColumn, $recoveryMeanConfigColumn, "
                + "$sharingMeanConfigColumn, $numberOfTicksConfigColumn, $SharingDebunkingConfigColumn, "
                + "$TicksToStartLosingInterestConfigColumn, $seedOSNConfigColumn) "
                + "VALUES "
                + "(\"%s\", %s, %s, %s, %s, %s, "
                + "%s, %s, %s, %s, "
                + "%s, %s, %s, %s, %s, %s, \"%s\")")

        private const val insertConfigSocialTemplate = ("INSERT INTO $configurationSocialFakeNewsTableName "
                + "($believersCountSocialColumn, $deniersCountSocialColumn, $susceptibleCountSocialColumn, "
                + "$averageFollowersSocialColumn, "
                + "$selectedTopologySocialColumn, $numberOfTicksSocialColumn, "
                + "$totalAgentsSocialColumn, $nodesInBarabasiSocialColumn, "
                + "$initialNodesInBarabasiSocialColumn, $nBotsSocialColumn, "
                + "$createInterestSocialColumn, $nOfInterestsSocialColumn, "
                + "$nOfInfluencersBelieversSocialColumn, $nOfInfluencersDeniersSocialColumn, "
                + "$nOfInfluencersSusceptiblesSocialColumn, "
                + "$nFollowersToBeInfluencerSocialColumn, $nBotsConnectionsSocialColumn, $pInflSocialColumn, "
                + "$pbelieveSocialColumn,"
                + "$pDenySocialColumn, $pVaccSocialColumn, $seedSocialColumn) "
                + "VALUES "
                + "(%s, %s, %s, %s, "
                + "\"%s\", %s, "
                + "%s, %s, "
                + "%s, %s, "
                + "%s, %s, "
                + "%s, %s, %s, "
                + "%s, %s, %s, %s, "
                + "%s, %s, \"%s\")")

        private const val getConfigOSNIdTemplate =
            ("SELECT $idConfigurationColumn FROM $configurationOSNTableName WHERE "
                    + "$believersCountSocialColumn = %s AND "
                    + "$deniersCountSocialColumn = %s AND "
                    + "$susceptibleCountSocialColumn = %s AND "
                    + "$averageFollowersSocialColumn = %s AND "
                    + "$selectedTopologySocialColumn = \"%s\" AND "
                    + "$numberOfTicksSocialColumn = %s AND "
                    + "$totalAgentsSocialColumn = %s AND "
                    + "$nodesInBarabasiSocialColumn = %s AND "
                    + "$initialNodesInBarabasiSocialColumn = %s AND "
                    + "$nBotsSocialColumn = %s AND "
                    + "$createInterestSocialColumn = %s AND "
                    + "$nOfInterestsSocialColumn = %s AND "
                    + "$nOfInfluencersBelieversSocialColumn = %s AND "
                    + "$nOfInfluencersDeniersSocialColumn = %s AND "
                    + "$nOfInfluencersSusceptiblesSocialColumn = %s AND "
                    + "$nFollowersToBeInfluencerSocialColumn = %s AND "
                    + "$nBotsConnectionsSocialColumn = %s AND "
                    + "$pInflSocialColumn = %s AND "
                    + "$pbelieveSocialColumn = %s AND "
                    + "$pDenySocialColumn = %s AND "
                    + "$pVaccSocialColumn = %s")

        private const val getConfigSocialIdTemplate =
            ("SELECT $idConfigurationSocialColumn FROM $configurationSocialFakeNewsTableName WHERE "
                    + "$believersCountSocialColumn = %s AND "
                    + "$deniersCountSocialColumn = %s AND "
                    + "$susceptibleCountSocialColumn = %s AND "
                    + "$averageFollowersSocialColumn = %s AND "
                    + "$selectedTopologySocialColumn = \"%s\" AND "
                    + "$numberOfTicksSocialColumn = %s AND "
                    + "$totalAgentsSocialColumn = %s AND "
                    + "$nodesInBarabasiSocialColumn = %s AND "
                    + "$initialNodesInBarabasiSocialColumn = %s AND "
                    + "$nBotsSocialColumn = %s AND "
                    + "$createInterestSocialColumn = %s AND "
                    + "$nOfInterestsSocialColumn = %s AND "
                    + "$nOfInfluencersBelieversSocialColumn = %s AND "
                    + "$nOfInfluencersDeniersSocialColumn = %s AND "
                    + "$nOfInfluencersSusceptiblesSocialColumn = %s AND "
                    + "$nFollowersToBeInfluencerSocialColumn = %s AND "
                    + "$nBotsConnectionsSocialColumn = %s AND "
                    + "$pInflSocialColumn = %s AND "
                    + "$pbelieveSocialColumn = %s AND "
                    + "$pDenySocialColumn = %s AND "
                    + "$pVaccSocialColumn = %s")

        private const val getconfigIdTemplate =
            "SELECT $idConfigurationColumn FROM $configurationOSNTableName WHERE $idConfigurationColumn " +
                    "= (SELECT MAX($idConfigurationColumn) FROM $configurationOSNTableName)"

        private const val getConfigIds = ("SELECT $idConfigurationColumn FROM $configurationOSNTableName")

        private const val getConfigs = "SELECT * FROM $configurationOSNTableName"

        private const val getBest10Configs = "SELECT * FROM $configurationOSNTableName, ${errorForNotLabeledOSNTable} WHERE ${datasetNotLabeledIdErrorForNotLabeledColumn} = %s AND ${idConfigurationColumn} = ${configurationIdColumn} ORDER BY ${nrmseErrorNotLabeledColumn} ASC LIMIT 10"

        private const val getBest10ConfigsForOSNLabeled = "SELECT *, (${errorForLabeledOSNTable}.${nrmseBelieversColumn} + ${errorForLabeledOSNTable}.${nrmseDeniersColumn}) as totalNRMSE FROM ${configurationOSNTableName}, ${errorForLabeledOSNTable} WHERE ${dataSetLabeledIdColumn} = %s AND ${idConfigurationColumn} = ${configurationIdColumn} ORDER BY totalNRMSE ASC LIMIT 10"

        private const val getConfigOSN = "SELECT * FROM $configurationOSNTableName WHERE $idConfigurationColumn = %s"

        private const val getConfigsSocial = "SELECT * FROM $configurationSocialFakeNewsTableName"

        private const val getBestConfigSocial = "SELECT *, (${errorForLabeledSocialTable}.${nrmseBelieversSocialColumn} + ${errorForLabeledSocialTable}.${nrmseDeniersSocialColumn}) as totalNrmse FROM ${configurationSocialFakeNewsTableName}, ${errorForLabeledSocialTable} WHERE ${dataSetLabeledIdColumn} = %s AND ${configurationIdColumn} = ${idConfigurationSocialColumn} ORDER BY totalNrmse ASC LIMIT 10"

        private const val getConfigSocialTemplate = "SELECT * FROM $configurationSocialFakeNewsTableName WHERE $idConfigurationSocialColumn = %s"

        private const val insertResultStepOSNTemplate = ("INSERT INTO $stepOSNTableName " +
                "($tickStepColumn, $believersStepColumn, $factCheckersStepColumn, $idConfigStepColumn, " +
                "$believersSharingColumn) VALUES " +
                "(%s, %s, %s, %s, %s)")

        private const val insertResultStepSocialTemplate = ("INSERT INTO $stepSocialTableName " +
                "($tickStepSocialColumn, $believersStepSocialColumn, $deniersStepSocialColumn, " +
                "$idConfigStepSocialColumn) VALUES " +
                "(%s, %s, %s, %s)")

        private const val getNumberOfUserInConfigTemplate =
            ("SELECT $agentsConfigColumn FROM $configurationOSNTableName WHERE $idConfigurationColumn = %s")

        private const val getNumberOfUserInConfigSocialTemplate =
            ("SELECT $totalAgentsSocialColumn FROM $configurationSocialFakeNewsTableName " +
                    "WHERE $idConfigurationSocialColumn = %s")

        private const val getBelieversSharingPerTickTemplate =
            ("SELECT $believersSharingColumn FROM $stepOSNTableName " +
                    "WHERE $idConfigStepColumn = %s AND $tickStepColumn = %d")

        private const val getBelieversPerTickTemplate =
            "SELECT $believersStepColumn FROM $stepOSNTableName " +
                    "WHERE $idConfigStepColumn = %s AND $tickStepColumn = %d"

        private const val getBelieversPerTickTemplateSocial =
            "SELECT $believersStepSocialColumn FROM $stepSocialTableName " +
                    "WHERE $idConfigStepSocialColumn = %s AND $tickStepSocialColumn = %d"

        private const val getDeniersPerTickTemplate =
            "SELECT $factCheckersStepColumn FROM $stepOSNTableName " +
                    "WHERE $idConfigStepColumn = %s AND $tickStepColumn = %d"

        private const val getDeniersPerTickTemplateSocial =
            "SELECT $deniersStepSocialColumn FROM $stepSocialTableName " +
                    "WHERE $idConfigStepSocialColumn = %s AND $tickStepSocialColumn = %d"

        private const val getListOfExecutionsQuery = "SELECT * FROM $configurationOSNTableName"

        private const val insertErrorForLabeled = "INSERT INTO $errorForLabeledOSNTable " +
                "($configurationIdColumn, $dataSetLabeledIdColumn, $rmseBelieversColumn, " +
                "$nrmseBelieversColumn, $rmseDeniersColumn, $nrmseDeniersColumn, $rmseTotalColumn) VALUES " +
                "(%d, %d, %,.4f, %,.4f, %,.4f, %,.4f, %,.4f)"

        private const val insertSocialErrorForLabeled = "INSERT INTO $errorForLabeledSocialTable " +
                "($ConfigurationIdSocialColumn, $DataSetLabeledIdSocialColumn, $rmseBelieversSocialColumn, " +
                "$nrmseBelieversSocialColumn, $rmseDeniersSocialColumn, " +
                "$nrmseDeniersSocialColumn, $rmseTotalSocialColumn) VALUES " +
                "(%d, %d, %,.4f, %,.4f, %,.4f, %,.4f, %,.4f)"

        private const val insertErrorForNotLabeled = "INSERT INTO $errorForNotLabeledOSNTable " +
                "($configurationIdErrorForNotLabeledColumn, $datasetNotLabeledIdErrorForNotLabeledColumn, " +
                "$rmseErrorNotLabeledColumn, $nrmseErrorNotLabeledColumn) VALUES " +
                "(%d, %d, %,.4f, %.4f)"

        private const val getNumberOfTicksSocial = "SELECT $numberOfTicksSocialColumn " +
                "FROM $configurationSocialFakeNewsTableName WHERE $idConfigurationSocialColumn = %s"

    }
}