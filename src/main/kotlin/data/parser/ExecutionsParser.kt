package data.parser

import org.apache.commons.csv.CSVParser

class ExecutionsParser(val parserForSteps: CSVParser,
                       val parserForConfig: CSVParser
) {

    companion object {
        const val configIdColumn = "run"
        const val botsColumn = "bots"
        const val initialNodesBarabasiColumn = "initial_nodes_in_barabasi"
        const val initialBelieversColumn = "initial_believers"
        const val topologyColumn = "topology"
        const val initialFactCheckersColumn = "initial_fact_checkers"
        const val recoveryMeanColumn = "recovery_mean"
        const val factCheckersConversion = "factcheckers_conversion"
        const val timeAccessForUsers = "time_access_for_users"
        const val difInterestsColumn = "dif_interests"
        const val timeAccessForBots = "time_access_for_bots"
        const val agentsColumn = "agents"
        const val randomSeedColumn = "randomSeed"
        const val vulnerabilityMeanColumn = "vulnerability_mean"
        const val sharingMeanCloumn = "sharing_mean"
        const val workWithTimeDynamicsColumn = "work_with_time_dynamics"
        const val influencersColumn = "influencers"
        const val nodeEdgesBarabasiColumn = "node_edges_in_barabasi"

        const val tickColumn = "tick"
        const val believersSharingColumn = "believersSharing"
        const val believersColumn = "believers"
        const val factCheckers = "factCheckers"

        const val believersSocialColumn = "Believers"
        const val deniersSocialColumn = "Fact Checkers"

        const val numberSteps = "number_steps"
        const val sharingDebunking = "sharing_debunking"
        const val ticksLostInterest = "ticks_lose_interest"

        const val configSocialIdColumn = "run"
        const val denyProbSocialColumn = "deny_prob"
        const val selectedTopologyColumn = "selected_topology"
        // const val initialNodesBarabasiColumn = "initial_nodes_in_barabasi"
        const val nFollowersInfluencersColumn = "n_followers_to_be_influencer"
        const val believeProbColumn = "believe_prob"
        const val nInfluencersBelieversColumn = "influencer_count_believers"
        const val nOfInterestsColumn = "number_of_interests"
        const val vaccProbColumn = "vacc_prob"
        const val nInfluencersSusceptiblesColumn = "influencer_count_susceptibles"
        const val influencersProbColumn = "influencer_prob"
        const val nBelieversColumn = "believer_count"
        const val nAgentsColumn = "user_count"
        const val createInterestsColumn = "create_interests"
        const val nTicksColumn = "number_of_ticks"
        const val nodesInBarabasiColumn = "nodes_in_barabasi"
        const val nBotsConnectionsColumn = "n_bots_connections"
        const val denierCountColumn = "denier_count"
        const val avgFollowersColumn = "avg_followers"
        const val nInfluencersDeniersColumn = "influencer_count_deniers"
        const val nBotsColumn = "bots_count"
        const val seedColumn = "randomSeed"
    }
}