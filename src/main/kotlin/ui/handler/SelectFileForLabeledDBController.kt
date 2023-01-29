package ui.handler

import datasetlabeled.parser.ParserDataSetTimed
import tornadofx.Controller
import java.io.File

class SelectFileForLabeledDBController: Controller() {

    private lateinit var parser : ParserDataSetTimed
    fun fileSelected(file: File) {
        println("File selected ${file.path}")
        parser = ParserDataSetTimed(file)
        parser.insertRecordsInDataBase()

    }
}