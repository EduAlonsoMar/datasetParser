package database.databaseResults.db

import database.FakeNewsDataBase
import org.apache.commons.csv.CSVParser
import java.sql.SQLException
import java.sql.Statement

class DataBaseModelExecution: FakeNewsDataBase(jdbcUrl, user, pass) {

    fun insertExecutionsParametersFromFile(parser: CSVParser, keyMap: MutableMap<String, Int>) {
        initializeConnection()

        val stmt: Statement? = connection?.createStatement()

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
                getConfigIdTemplate,
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

            stmt?.let { statement ->
                try {
                    statement.execute(insertQuery)
                    try {
                        val resultQuery = statement.executeQuery(getIdQuery)
                        if (resultQuery.next()) {
                            keyMap.put(csvRecord.get("run"), resultQuery.getInt("idConfiguration"))
                        }
                    } catch (e: SQLException) {
                        logQueryError(getIdQuery, e)
                    }
                } catch (e: SQLException) {
                    logQueryError(insertQuery, e)
                }
            }
        }
    }

    fun insertExecutionStepsFromFile(parser: CSVParser, keyMap: MutableMap<String, Int>) {
        initializeConnection()

        val stmt: Statement? = connection?.createStatement()

        for (csvRecord in parser) {
            var insertSept = String.format(
                insertResultStepTemplate,
                csvRecord.get("tick"),
                csvRecord.get("Believer"),
                csvRecord.get("Fact_Checker"),
                keyMap[csvRecord.get("run")]
            )
            stmt?.let { statement ->
                try {
                    statement.execute(insertSept)
                } catch (e: SQLException) {
                    logQueryError(insertSept, e)
                }
            }
        }

    }

    fun getConfigurationIds(): List<String>? {
        initializeConnection()

        val stmt = connection?.createStatement()


        stmt?.let { statement ->
            try {
                val resultQuer = statement.executeQuery(getConfigIds)
                val resultList = mutableListOf<String>()
                while (resultQuer.next()) {
                    resultList.add(resultQuer.getInt("idConfiguration").toString())
                }
                return resultList
            } catch (e: SQLException) {
                logQueryError(getConfigIds, e)
            }
        }

        return null
    }

    fun getTotalUsersForconfig(configId: String): Int? {
        initializeConnection()
        val stmt = connection?.createStatement()
        val getQuery = String.format(
            getNumberOfUserInConfigTemplate,
            configId
        )
        try {
            stmt?.let { statement ->
                val resultQuery = statement.executeQuery(getQuery)
                if (resultQuery.next()) {
                    return resultQuery.getInt("idConfiguration")
                }
            }
        } catch (e: SQLException) {
            logQueryError(getQuery, e)
        }

        return null
    }

    fun getBeleiversInStep(configId: String, tick: Int): Int? {
        initializeConnection()
        val stmt = connection?.createStatement()
        val getQuery = String.format(
            getBeleiversPerTickTemplate,
            configId,
            tick
        )

        try {
            stmt?.let { statement ->
                val resultQuery = statement.executeQuery(getQuery)
                if (resultQuery.next()) {
                    return resultQuery.getInt("believers")
                }
            }
        } catch (e: SQLException) {
            logQueryError(getQuery, e)
        }

        return null
    }

    companion object {
        private const val jdbcUrl = "jdbc:mysql://localhost:3306/OSNModelExecutions"
        private const val user = "fakenews"
        private const val pass = "fakenews"


        private const val insertConfigTemplate = ("INSERT INTO Configuration "
                + "(topology, agents, influencers, Bots, WorkWithTimeDynamics, TimeAccessForCommonUsers, "
                + "TimeAccessForBots, InitialNodesInBarabasi, NodesInbarabasi, NumberOfInitialBeleivers, "
                + "VulnerabilityMean, VulnerabilityVariance, RecoveryMean, RecoveryVariance, SharingMean, SharingVariance) "
                + "VALUES "
                + "(\"%s\", %s, %s, %s, %s, %s, "
                + "%s, %s, %s, %s, "
                + "%s, %s, %s, %s, %s, %s)")

        private const val getConfigIdTemplate = ("SELECT idConfiguration FROM Configuration WHERE "
        + "topology = \"%s\" AND "
        + "agents = %s AND "
        + "influencers = %s AND "
        + "Bots = %s AND "
        + "WorkwithTimeDynamics = %s AND "
        + "TimeAccessForCommonUsers = %s AND "
        + "TimeAccessForBots = %s AND "
        + "InitialNodesInBarabasi = %s AND "
        + "NodesInBarabasi = %s AND "
        + "NumberOfInitialBeleivers = %s AND "
        + "VulnerabilityMean = %s AND "
        + "VulnerabilityVariance = %s AND "
        + "RecoveryMean = %s AND "
        + "RecoveryVariance = %s AND "
        + "SharingMean = %s AND "
        + "SharingVariance = %s")

        private const val getConfigIds = ("SELECT idConfiguration FROM Configuration")

        private const val insertResultStepTemplate = ("INSERT INTO Step " +
                "(tick, believers, fact_checkers, idConfig) VALUES " +
                "(%s, %s, %s, %s)")

        private const val getNumberOfUserInConfigTemplate = ("SELECT agents FROM Configuration WHERE idConfiguration = %s")

        private const val getBeleiversPerTickTemplate = ("SELECT believers FROM Step WHERE idConfig = %s AND tick = %d")

    }
}