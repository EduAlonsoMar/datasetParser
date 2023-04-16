package data.datasource

import data.parser.ExecutionsParser
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.File

class ExecutionsParserDataSource {
    fun getParser( fileWithSteps: File,
            fileWithConfig: File): ExecutionsParser {
        return ExecutionsParser(
            CSVParser(fileWithSteps.bufferedReader(), CSVFormat.DEFAULT.withFirstRecordAsHeader()
                .withIgnoreHeaderCase().withTrim()),
            CSVParser(fileWithConfig.bufferedReader(), CSVFormat.DEFAULT.withFirstRecordAsHeader()
                .withIgnoreHeaderCase().withTrim())
        )
    }
}