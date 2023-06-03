import javafx.stage.Stage
import koin.appModule
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import org.koin.core.context.GlobalContext.startKoin
import java.io.File
import tornadofx.*
import ui.HomeWindow
import ui.css.AppStyle

class MyApp: App(HomeWindow::class, AppStyle::class) {
    override fun start(stage: Stage) {
        with (stage) {
            minWidth = 900.0
            minHeight = 200.0
            super.start(this)
        }
    }
    init {
        reloadStylesheetsOnFocus()
    }
}
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

    startKoin {
        modules(appModule)
    }


    launch<MyApp>()
    println("Window launch")
}