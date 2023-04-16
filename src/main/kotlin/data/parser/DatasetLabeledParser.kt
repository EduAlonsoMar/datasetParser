package data.parser

import org.apache.commons.csv.CSVParser

data class DatasetLabeledParser( val parser: CSVParser, val datasetName: String) {

    companion object {
        const val dateColumn = "date"
        const val userIdColumn = "userID"
        const val labelColumn = "label"
    }
}