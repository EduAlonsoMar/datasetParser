package data.repository

import data.database.model.*
import data.datasource.ExecutionsDataSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ExecutionsResultsRepository : KoinComponent {

    private val executionsDataSource: ExecutionsDataSource by inject()
    // val totalUsers =

    fun insertConfigurationOSN(config: ConfigurationOSN): Int {
        return executionsDataSource.insertConfigurationOSN(config)
    }

    fun insertConfigurationSocial(config: ConfigurationSocialFakeNews): Int {
        return executionsDataSource.insertConfigurationSocialFakeNews(config)
    }

    fun insertStepOSN(step: Step) {
        return executionsDataSource.insertStepOSN(step)
    }

    fun insertStepSocial(step: Step) {
        return executionsDataSource.insertStepSocialFakeNews(step)
    }

    fun getConfigurationIds(): List<String> {
        return executionsDataSource.getConfigurations().map { it.id.toString() }
    }

    fun getConfigurations(): List<ConfigurationOSN> {
        return executionsDataSource.getConfigurations()
    }

    fun getConfigurationsSocial(): List<ConfigurationSocialFakeNews> {
        return executionsDataSource.getConfigurationsSocial()
    }

    fun getConfigurationsSocialIds(): List<String> {
        return executionsDataSource.getConfigurationsSocial().map { it.id.toString() }
    }

    fun getTotalUsers(configId: String): Int {
        return executionsDataSource.getTotalUsersForConfig(configId) ?: 0
    }

    fun getTotalUsersSocial(configId: String): Int {
        return executionsDataSource.getTotalUsersSocialForConfig(configId) ?: 0
    }

    fun getNumberOfUserSendingInAnHour(configId: String, hourInSequence: Int): Int {

        return executionsDataSource.getMaxNumberOfBelieversSharingInStepNormalized(
            configId,
            hourInSequence,
            normalizerForNotLabeled
        )

    }

    fun getNumberOfBelieversInDay(configId: String, dayInSequence: Int): Int {
        return executionsDataSource.getMaxNumberOfBelieversInStepNormalized(
            configId,
            dayInSequence,
            normalizedForLabeled
        )
    }

    fun getNumberOfBelieversInDaySocial(configId: String, dayInSequence: Int): Int {
        return executionsDataSource.getMaxNumberOfBelieversInStepNormalizedSocial(
            configId,
            dayInSequence,
            normalizedForLabeled
        )
    }

    fun getNumberOfDeniersInDay(configId: String, stepInSequence: Int): Int {
        return executionsDataSource.getMaxNumberOfDeniersInStepNormalized(
            configId,
            stepInSequence,
            normalizedForLabeled
        )
    }

    fun getNumberOfDeniersInDaySocial(configId: String, stepInSequence: Int): Int {
        return executionsDataSource.getMaxNumberOfDeniersInStepNormalizedSocial(
            configId,
            stepInSequence,
            normalizedForLabeled
        )
    }

    fun getNumberOfTicksSocial(configId: String): Int {
        return executionsDataSource.getNumberOfTicksSocial(configId)
    }

    fun insertErrorNotLabeled(error: ErrorForNotLabeled) {
        return executionsDataSource.insertErrorNotLabeled(error)
    }

    fun insertErrorLabeled(error: ErrorForLabeled) {
        return executionsDataSource.insertErrorForLabeled(error)
    }

    fun insertErrorSocialLabeled(error: ErrorSocialForLabeled) {
        return executionsDataSource.insertErrorSocialForLabeled(error)
    }

    fun normalizedAddedValueForLabeledSocial(configId: String): Int {
        return executionsDataSource.getNumberOfTicksSocial(configId) / 15
    }

    companion object {
        const val stepsForExecution = 100
        const val stepsForExecutionSocial = 400
        const val normalizerForNotLabeled = 3
        const val normalizedForLabeled = 6
    }
}