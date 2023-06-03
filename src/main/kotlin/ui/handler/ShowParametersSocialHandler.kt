package ui.handler

import data.database.model.ConfigurationSocialFakeNews
import data.repository.ExecutionsResultsRepository
import tornadofx.Controller

class ShowParametersSocialHandler: Controller() {
    private val executionsResultsRepository = ExecutionsResultsRepository()

    fun getConfiguration(id: String): ConfigurationSocialFakeNews {
        return executionsResultsRepository.getConfigurationSocial(Integer.parseInt(id))
    }
}