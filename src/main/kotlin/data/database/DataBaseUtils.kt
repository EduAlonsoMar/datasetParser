package data.database

object DataBaseUtils {

    fun sanitizeText(title: String): String {
        return title.replace("\"","\\\"")
    }

    fun sanitizeUserName(name: String): String {
        val re = Regex("[^A-Za-z0-9 ]")
        return re.replace(name, "?")
    }
}