import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import ui.MainWindow
import java.io.File
import tornadofx.*

class MyApp: App(MainWindow::class)
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


fun main(args: Array<String>) {

    //val appUi = MainWindow()
    // appUi.start(datasetTimedHandler)

    //val jdbcUrl = "jdbc:mysql://localhost:3306/FakeNewsDataSet"

    //val connection = DriverManager.getConnection(jdbcUrl, "fakenews", "fakenews")


    launch<MyApp>()
    println("Window launch")

    //val stmt = connection.createStatement()

    //println(connection.isValid(0))

    //DataBaseInsertion().insertNewsFake(parseNewsFake().getParserForFakeNews(), connection)
    //DataBaseInsertion().insertUsers(parseNewsFake().getParserForTuitsFakeNews(), connection)
    //DataBaseInsertion().insertTuits(parseNewsFake().getParserForTuitsNewsConnection(), parseNewsFake().getParserForTuitsFakeNews(), connection)
}