package database.databasenotlabeled.parser

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.File

class parseNewsFake {

    private var insertNewsTemplate = "INSERT INTO News (index, title, content, fake) VALUES (%s, \"%s\", \"%s\", %d)"

    fun checkFirstLine() {
        val bufferedReader = File("/Users/edualonso/Documents/UNED/TFG/datasets/CoAID/05-01-2020/NewsFakeCOVID-19.csv").bufferedReader()
        val csvParser = CSVParser(bufferedReader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
            .withIgnoreHeaderCase().withTrim())
        for (csvRecord in csvParser) {


            val statement = String.format(insertNewsTemplate, csvRecord.get(0), csvRecord.get("title"), csvRecord.get("content"), 1)
            println(statement)
        }
    }

    fun getParserForFakeNews(): CSVParser {
        val bufferedReader = File("/Users/edualonso/Documents/UNED/TFG/datasets/CoAID/05-01-2020/NewsFakeCOVID-19.csv").bufferedReader()
        return CSVParser(bufferedReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())
    }

    fun getParserForTuitsFakeNews(): CSVParser {
        val bufferedReader = File("/Users/edualonso/Documents/UNED/TFG/datasets/CoAID/05-01-2020/CNewsFakeCOVID-19_hydrated_tweets_only.csv").bufferedReader()
        return CSVParser(bufferedReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())
    }

    fun getParserForTuitsNewsConnection(): CSVParser {
        val bufferedReader = File("/Users/edualonso/Documents/UNED/TFG/datasets/CoAID/05-01-2020/NewsFakeCOVID-19_tweets.csv").bufferedReader()
        return CSVParser(bufferedReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())
    }
}