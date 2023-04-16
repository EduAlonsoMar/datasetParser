package data.parser

import org.apache.commons.csv.CSVParser

data class DatasetNotLabeledParser(val parserWithNews: CSVParser,
    val parserWithTuits: CSVParser,
    val parserWithTuitsAndNews: CSVParser) {

    companion object {
        const val datasetNotLabeledTitleColumn = "title"
        const val datasetNotLabeledContentColumn = "content"
        const val newsAndTuitsNewsIndexColumn = "index"
        const val newsAndTuitsTuitIdColumn = "tweet_id"

        const val tuitsUserIdColumn = "user_id"
        const val tuitsUserNameColumn = "user_name"
        const val tuitsFollowersColumn = "user_followers_count"
        const val tuitsFriendsClolumn = "user_friends_count"
        const val tuitsIdColumn = "id"
        const val tuitsTextColumn = "text"
        const val tuitsTimestampColumn = "created_at"

    }
}