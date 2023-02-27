package database.databaseResults.parser

import database.databaseResults.db.DataBaseModelExecution
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.File

class ParserResultBatch(var fileToParse: File? = null, val idMap: MutableMap<String, Int>) {

    private var csvParser: CSVParser? = null

    private val dataBaseHandler = DataBaseModelExecution()

    @kotlin.jvm.Throws(RuntimeException::class)
    fun insertRecordsInDataBase () {

        if (fileToParse == null) {
            throw (RuntimeException("file To parse must not be null"))
        }

        csvParser = CSVParser(fileToParse?.bufferedReader(), CSVFormat.DEFAULT.withFirstRecordAsHeader()
            .withIgnoreHeaderCase().withTrim())

        csvParser?.let { parser ->
            dataBaseHandler.insertExecutionStepsFromFile(parser, idMap)
        }

        dataBaseHandler.closeConnection()

    }


}