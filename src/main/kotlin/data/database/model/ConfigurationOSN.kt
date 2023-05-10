package data.database.model

data class ConfigurationOSN(
    val topology: String? = null,
    val agents: String? = null,
    val influencers: String? = null,
    val bots: String? = null,
    val workWithTimeDynamics: String? = null,
    val timeAccessForCommonUsers: String? = null,
    val timeAccessForBots: String? = null,
    val initialNodesBarabasi: String? = null,
    val nodesInBarabasi: String? = null,
    val numberOfInitialBelievers: String? = null,
    val vulnerabilityMean: String? = null,
    val recoveryMean: String? = null,
    val sharingMean: String? = null,
    val numberOfTicks: String? = null,
    val sharingDebunking: String? = null,
    val ticksToStartLosingInterest: String? = null
) {
    var id: Int? = null
}