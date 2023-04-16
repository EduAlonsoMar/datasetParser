package data.repository

import data.database.model.Configuration
import data.database.model.ErrorForLabeled
import data.database.model.ErrorForNotLabeled
import data.database.model.Step
import data.datasource.ExecutionsDataSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ExecutionsResultsRepository: KoinComponent {

    private val executionsDataSource: ExecutionsDataSource by inject()
    // val totalUsers =

    fun insertConfiguration(config: Configuration): Int {
        return executionsDataSource.insertConfiguration(config)
    }

    fun insertStep(step: Step) {
        return executionsDataSource.insertStep(step)
    }

    fun getConfigurationIds(): List<String> {
        return executionsDataSource.getConfigurations().map { it.id.toString() }
    }

    fun getConfigurations(): List<Configuration> {
        return executionsDataSource.getConfigurations()
    }

    fun getTotalUsers(configId: String): Int {
        return executionsDataSource.getTotalUsersForconfig(configId) ?: 0
    }

    fun getNumberOfUserSendingInAnHour(configId: String, hourInSequence: Int): Int {

        return executionsDataSource.getMaxNumberOfBelieversSharingInStepNormalized(configId, hourInSequence, normalizerForNotLabeled)

    }

    fun getNumberOfBelieversInDay(configId: String, dayInSequence: Int): Int {
        return executionsDataSource.getMaxNumberOfBelieversInStepNormalized(configId, dayInSequence, normalizedForLabeled)
    }

    fun getNumberOfDeniersInDay(configId: String, stepInSequence: Int): Int {
        return executionsDataSource.getMaxNumberOfDeniersInStepNormalized(configId, stepInSequence, normalizedForLabeled)
    }

    fun insertErrorNotLabeled(error: ErrorForNotLabeled) {
        return executionsDataSource.insertErrorNotLabeled(error)
    }

    fun insertErrorLabeled(error: ErrorForLabeled) {
        return executionsDataSource.insertErrorForLabeled(error)
    }



    companion object {
        const val stepsForExecution = 100
        const val normalizerForNotLabeled = 3
        const val normalizedForLabeled = 6
    }
}