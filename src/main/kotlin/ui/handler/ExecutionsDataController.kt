package ui.handler

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
}