package data.database.model

data class ConfigurationSocialFakeNews(

            val believersCount: String? = null,
            val deniersCount: String? = null,
            val susceptibleCount: String? = null,
            val averageFollowers: String? = null,
            val selectedTopology: String? = null,
            val numberOfTicks: String? = null,
            val totalAgents: String? = null,
            val nodesInBarabasi: String? = null,
            val initialNodesInBarabasi: String? = null,
            val nBots: String? = null,
            val createInterest: String? = null,
            val nOfInterests: String? = null,
            val nOfInfluencersBelievers: String? = null,
            val nOfInfluencersDeniers: String? = null,
            val nOfInfluencersSusceptibles: String? = null,
            val nFollowersToBeInfluencer: String? = null,
            val nBotsConnections: String? = null,
            val pInfl: String? = null,
            val pbelieve: String? = null,
            val pDeny: String? = null,
            val pVacc: String? = null
) {
    var id: Int? = null
}