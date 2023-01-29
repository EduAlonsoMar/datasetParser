package datasetlabeled.parser

import datasetlabeled.db.DataBaseInsertions
import datasetlabeled.db.DataBaseLabeled
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.io.File

class ParserDataSetTimed (var fileToParse: File? = null) {

    private var csvParser: CSVParser? = null

    private val dataBaseHandler = DataBaseInsertions()

    @kotlin.jvm.Throws(RuntimeException::class)
    fun insertRecordsInDataBase () {

        if (fileToParse == null) {
            throw (RuntimeException("file To parse must not be null"))
        }

        dataBaseHandler.insertDataSetDescription(fileToParse!!.name, "")

        csvParser = CSVParser(fileToParse?.bufferedReader(), CSVFormat.DEFAULT.withFirstRecordAsHeader()
            .withIgnoreHeaderCase().withTrim())

        csvParser?.let { parser ->
            dataBaseHandler.insertDatasetTimedRecords(fileToParse!!.name, parser)
        }

        dataBaseHandler.closeConnection()

    }
}