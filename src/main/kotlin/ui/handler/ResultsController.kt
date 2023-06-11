package ui.handler

import data.repository.DatasetLabeledRepository
import data.repository.DatasetNotLabeledRepository
import data.repository.ExecutionsResultsRepository
import tornadofx.Controller

class ResultsController: Controller() {

    private val datasetLabeledRepository = DatasetLabeledRepository()
    private val datasetNotLabeledRepository = DatasetNotLabeledRepository()
    private val executionsResultsRepository = ExecutionsResultsRepository()

    fun getDatasetListOnlyNames(): List<String> {
        return datasetLabeledRepository.getDatasetListOnlyNames()
    }

    fun getTitleForDataSetsWithMostUsers(): List<String> {
        return datasetNotLabeledRepository.getTitleForDataSetsWithMostUsers()
    }

    fun getConfigurationIds(): List<String> {
        return executionsResultsRepository.getConfigurationIds()
    }

    fun getConfigurationsSocialIds(): List<String> {
        return executionsResultsRepository.getConfigurationsSocialIds()
    }

    fun getBestOSNConfigurationsForDataSetNotLabeled(dataset: String): List<String> {
        return executionsResultsRepository.getBestOSNConfigurationsForDataSetNotLabeled(dataset)
    }

    fun getBestOSNConfigurationsForDataSetNotLabeledDays(dataset: String): List<String> {
        return executionsResultsRepository.getBestOSNConfigurationsForDataSetNotLabeledDays(dataset)
    }

    fun getBestOSNConfigurationsForDataSetLabeled(dataset: String): List<String> {
        return executionsResultsRepository.getBestOSNConfigurationsForDataSetLabeled(dataset)
    }

    fun getBestSocialConfigurationForDataSetLabeled(dataset: String): List<String> {
        return executionsResultsRepository.getBestSocialConfigurationForDataSetLabeled(dataset)
    }
}