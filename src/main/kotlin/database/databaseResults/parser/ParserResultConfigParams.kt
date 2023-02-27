package database.databaseResults.parser

import database.databaseResults.db.DataBaseModelExecution
import database.databaselabeled.db.DataBaseInsertions
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.File

class ParserResultConfigParams (var fileToParse: File? = null) {

    private var csvParser: CSVParser? = null

    private val dataBaseHandler = DataBaseModelExecution()

    var idMap = mutableMapOf<String, Int>()

    @kotlin.jvm.Throws(RuntimeException::class)
    fun insertRecordsInDataBase () {

        if (fileToParse == null) {
            throw (RuntimeException("file To parse must not be null"))
        }

        csvParser = CSVParser(fileToParse?.bufferedReader(), CSVFormat.DEFAULT.withFirstRecordAsHeader()
            .withIgnoreHeaderCase().withTrim())

        csvParser?.let { parser ->
            dataBaseHandler.insertExecutionsParametersFromFile(parser, idMap)
        }

        dataBaseHandler.closeConnection()

    }
}