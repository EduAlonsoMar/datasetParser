package ui.handler

import database.databaseResults.parser.ParserResultBatch
import database.databaseResults.parser.ParserResultConfigParams
import database.databaselabeled.parser.ParserDataSetTimed
import tornadofx.Controller
import java.io.File

class SelectFileForBatchResultParsing: Controller() {

    private lateinit var parserConfigParams: ParserResultConfigParams
    private lateinit var parser: ParserResultBatch

    fun fileSelected(file: File) {
        val nameSplitted = file.name.split(".")
        val pathSplitted = file.path.split("/")
        var configParamsFileName: String = ""
        var i = 0
        while ( i < (pathSplitted.size - 1)) {
            configParamsFileName += pathSplitted[i]+"/"
            i++
        }
        i=0
        while( i < (nameSplitted.size - 1)) {
            configParamsFileName += nameSplitted[i]+"."
            i++
        }
        configParamsFileName += "batch_param_map" + ".txt"
        val confiParamsFile = File(configParamsFileName)
        println("File selected ${file.path}")
        parserConfigParams = ParserResultConfigParams(confiParamsFile)
        parser = ParserResultBatch(file, parserConfigParams.idMap)
        parserConfigParams.insertRecordsInDataBase()
        parser.insertRecordsInDataBase()
    }
}