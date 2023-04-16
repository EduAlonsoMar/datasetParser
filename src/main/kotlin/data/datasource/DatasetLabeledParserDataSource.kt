package data.datasource

import data.parser.DatasetLabeledParser
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.File

class DatasetLabeledParserDataSource {

    fun getLabeledParser(fileToParse: File): DatasetLabeledParser {

        return DatasetLabeledParser(CSVParser(fileToParse?.bufferedReader(), CSVFormat.DEFAULT.withFirstRecordAsHeader()
            .withIgnoreHeaderCase().withTrim()),
            fileToParse!!.name)
    }


}