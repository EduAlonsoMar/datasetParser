package database

import datamodel.Tuit
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import util.DateTimeUtils
import java.sql.Connection
import java.sql.SQLIntegrityConstraintViolationException
import java.sql.Statement

class DataBaseInsertion {
    private var insertNewsTemplate = "INSERT INTO News (id, title, content, fake) VALUES (%s, \"%s\", \"%s\", %d)"
    private var insertUserTemplate = "INSERT INTO User (idUser, userName, followers, friends) VALUES (%s, \"%s\", %s, %s)"
    private var insertTuitTemplate = "INSERT INTO Tuit (idTuit, creation_date, text, news_index, user_idUser) VALUES (%s, \"%s\", \"%s\", %s, %s)"

    private lateinit var listNewsTuits: ArrayList<TuitNewsRecord>

    private fun sanitizeText(title: String): String {
        return title.replace("\"","\\\"")
    }

    fun insertNewsFake(csvParser: CSVParser, connection: Connection) {
        var stmt: Statement = connection.createStatement()
        stmt.execute("DELETE FROM News")
        for(csvRecord in csvParser) {
            var title = sanitizeText(csvRecord.get("title"))
            var content = sanitizeText(csvRecord.get("content"))
            val query = String.format(insertNewsTemplate, csvRecord.get(0), title, content, 1)
            println(query)
            if (!stmt.execute(query)) {
                println("statement with query $query failed")
            }
        }

    }

    private fun sanitizeUserName(name: String): String {
        val re = Regex("[^A-Za-z0-9 ]")
        return re.replace(name, "?")
    }

    fun insertUsers(csvParser: CSVParser, connection: Connection) {
        val stmt: Statement = connection.createStatement()
        stmt.execute("DELETE FROM User")
        var query: String
        var userName: String
        for (csvRecord in csvParser) {
            userName = sanitizeUserName(csvRecord.get("user_name"))
            query = String.format(insertUserTemplate, csvRecord.get("user_id"), userName, csvRecord.get("user_followers_count"), csvRecord.get("user_friends_count"))
            println(query)
            try {
                if (!stmt.execute(query)) {
                    println("statment with query $query failed")
                }
            } catch (e: SQLIntegrityConstraintViolationException) {
                println("User $userName already inserted")
            }
        }
    }

    private fun convertCSVParserIntoAList(csvParser: CSVParser): ArrayList<TuitNewsRecord> {
        val list = arrayListOf<TuitNewsRecord>()
        for (record in csvParser) {
            list.add(TuitNewsRecord(record.get("index"), record.get("tweet_id")))
        }

        return list
    }

    private fun searchNewsId(tuitId: String, csvParserNewsTuits: ArrayList<TuitNewsRecord>): String? {
        println("Searching tuit $tuitId")
        val iterator = csvParserNewsTuits.iterator()
        var tuitInNews: String
        var tmp: CSVRecord
        var i: Int
        for(record in csvParserNewsTuits) {
            if (record.tuitId == tuitId) {
                return record.newsId
            }
        }
        return null
    }

    fun insertTuits(csvParserNewsTuits: CSVParser, csvParserTuitsExtended: CSVParser, connection: Connection) {
        val stmt: Statement = connection.createStatement()
        stmt.execute("DELETE FROM Tuit")
        var tuitId: String
        var text: String
        var textfinal: String
        var timestamp: String
        listNewsTuits = convertCSVParserIntoAList(csvParserNewsTuits)
        for(csvRecord in csvParserTuitsExtended) {
            tuitId = csvRecord.get("id")
            searchNewsId(tuitId, listNewsTuits)?.let { newsIndex ->
                text = sanitizeText(csvRecord.get("text"))
                textfinal = sanitizeUserName(text)
                timestamp = DateTimeUtils.convertTimeToDateTime(csvRecord.get("created_at"))
                val query = String.format(insertTuitTemplate, tuitId, timestamp, textfinal, newsIndex, csvRecord.get("user_id"))
                println(query)
                if (!stmt.execute(query)) {
                    println("\t\t Failed!!")
                }
            }
        }
    }
}