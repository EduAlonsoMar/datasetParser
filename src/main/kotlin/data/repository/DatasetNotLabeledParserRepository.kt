package data.repository

import data.datasource.DatasetNotLabeledParserDataSource
import data.parser.DatasetNotLabeledParser
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class DatasetNotLabeledParserRepository : KoinComponent {

    private val datasetNotLabeledParserDataSource: DatasetNotLabeledParserDataSource by inject()

    fun getNotLabeledParser(
        fileWithNewsToParse: File,
        fileWithTuitsToParse: File,
        fileWithTuitsAndNewsToParse: File
    ): DatasetNotLabeledParser {
        return datasetNotLabeledParserDataSource.getParser(
            fileWithNewsToParse,
            fileWithTuitsToParse,
            fileWithTuitsAndNewsToParse
        )
    }
}