package data.repository

import data.datasource.DatasetLabeledParserDataSource
import data.parser.DatasetLabeledParser
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class DatasetLabeledParserRepository(): KoinComponent {

    private val datasetLabeledParserDataSource: DatasetLabeledParserDataSource by inject()

    fun getParserForFile(file: File): DatasetLabeledParser {
        return datasetLabeledParserDataSource.getLabeledParser(file)
    }



}