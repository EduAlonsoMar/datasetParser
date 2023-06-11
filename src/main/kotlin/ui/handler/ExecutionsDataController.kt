package ui.handler

import data.database.model.ConfigurationOSN
import data.database.model.ConfigurationSocialFakeNews
import data.repository.ExecutionsResultsRepository
import tornadofx.Controller

class ExecutionsDataController: Controller() {

    private val executionsResultsRepository = ExecutionsResultsRepository()

    fun getConfigSocial(executionId: String?): ConfigurationSocialFakeNews {
        return executionsResultsRepository.getConfigurationSocial(
            Integer.parseInt(executionId)
        )
    }

    fun getConfigOSN(executionId: String?): ConfigurationOSN {
        return executionsResultsRepository.getConfigurationOSN(
            Integer.parseInt(executionId)
        )
    }

    fun getNrsmeTotalOSN(datasetLabeledTitle: String, executionId: Int): Double {
        return executionsResultsRepository.getNrmseTotalOSN(datasetLabeledTitle, executionId)
    }

    fun getNrsmeTotalSocial(datasetLabeledTitle: String, executionId: Int): Double {
        return executionsResultsRepository.getNrmseTotalSocial(datasetLabeledTitle, executionId)
    }
}