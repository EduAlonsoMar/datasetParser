package data.datasource

import data.parser.DatasetNotLabeledParser
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.File

class DatasetNotLabeledParserDataSource(
) {


    fun getParser(fileWithNewsToParse: File,
                  fileWithTuitsToParse: File,
                  fileWithTuitsAndNewsToParse: File): DatasetNotLabeledParser {
        return DatasetNotLabeledParser(
            CSVParser(fileWithNewsToParse.bufferedReader(), CSVFormat.DEFAULT.withFirstRecordAsHeader()
                .withIgnoreHeaderCase().withTrim()),
            CSVParser(fileWithTuitsToParse.bufferedReader(), CSVFormat.DEFAULT.withFirstRecordAsHeader()
                .withIgnoreHeaderCase().withTrim()),
            CSVParser(fileWithTuitsAndNewsToParse.bufferedReader(), CSVFormat.DEFAULT.withFirstRecordAsHeader())
        )
    }

/*    fun checkFirstLine() {
        val bufferedReader = fileToParse.bufferedReader()
        val csvParser = CSVParser(bufferedReader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
            .withIgnoreHeaderCase().withTrim())
        for (csvRecord in csvParser) {


            val statement = String.format(insertNewsTemplate, csvRecord.get(0), csvRecord.get("title"), csvRecord.get("content"), 1)
            println(statement)
        }
    }*/

/*    fun getParserForFakeNews(): CSVParser {
        val bufferedReader = fileToParse.bufferedReader()
        return CSVParser(bufferedReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())
    }*/

/*    fun getParserForTuitsFakeNews(): CSVParser {
        val bufferedReader = fileToParse.bufferedReader()
        return CSVParser(bufferedReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())
    }*/

/*    fun getParserForTuitsNewsConnection(): CSVParser {
        val bufferedReader = fileToParse.bufferedReader()
        return CSVParser(bufferedReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())
    }*/
}