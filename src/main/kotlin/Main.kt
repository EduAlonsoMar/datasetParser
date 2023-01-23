import database.DataBaseInsertion
import datasetlabeled.ParserDataSetTimed
import datasettreatment.parseNewsFake
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import ui.MainWindow
import ui.handler.DatasetTimedSelected
import java.io.BufferedReader
import java.io.File
import java.nio.file.Paths
import java.sql.DriverManager

private fun convertDataSetToOnlyTuitsToHydrate() {
    val bufferedReader = File("/Users/edualonso/Documents/UNED/TFG/datasets/CoAID/05-01-2020/NewsFakeCOVID-19_tweets.csv").bufferedReader()
    val bufferedWriter = File("/Users/edualonso/Documents/UNED/TFG/datasets/CoAID/05-01-2020/CNewsFakeCOVID-19_tweets_only.csv").bufferedWriter()
    val csvParser = CSVParser(bufferedReader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
        .withIgnoreHeaderCase().withTrim())
    val csvPrinter = CSVPrinter(bufferedWriter, CSVFormat.DEFAULT)

    var i = 0
    for (csvRecord in csvParser) {
        i++
        csvPrinter.printRecord(csvRecord.get("tweet_id"))
    }

    csvPrinter.flush()
    csvPrinter.close()
}

class DefaultDatasetTimedSelected : DatasetTimedSelected {

    lateinit var parser : ParserDataSetTimed

    override fun fileSelected(file: File) {
        println("File selected ${file.path}")
        parser = ParserDataSetTimed(file)
        parser.insertRecordsInDataBase()

    }

}

fun main(args: Array<String>) {

    val datasetTimedHandler = DefaultDatasetTimedSelected()

    val appUi = MainWindow()
    appUi.start(datasetTimedHandler)

    //val jdbcUrl = "jdbc:mysql://localhost:3306/FakeNewsDataSet"

    //val connection = DriverManager.getConnection(jdbcUrl, "fakenews", "fakenews")

    println("Window launch")

    //val stmt = connection.createStatement()

    //println(connection.isValid(0))

    //DataBaseInsertion().insertNewsFake(parseNewsFake().getParserForFakeNews(), connection)
    //DataBaseInsertion().insertUsers(parseNewsFake().getParserForTuitsFakeNews(), connection)
    //DataBaseInsertion().insertTuits(parseNewsFake().getParserForTuitsNewsConnection(), parseNewsFake().getParserForTuitsFakeNews(), connection)
}