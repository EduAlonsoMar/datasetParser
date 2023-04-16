package data.repository

import data.datasource.ExecutionsParserDataSource
import data.parser.ExecutionsParser
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class ExecutionsParserRepository(): KoinComponent {

    private val executionsParserDataSource: ExecutionsParserDataSource by inject()

    fun getParser(fileWithSteps: File,
                  fileWithConfig: File
    ): ExecutionsParser {
        return executionsParserDataSource.getParser(fileWithSteps, fileWithConfig)
    }


}