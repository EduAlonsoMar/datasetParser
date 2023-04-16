package data.database.model

data class Opinion(
    val date: String,
    val userId: String,
    val label: String,
    val dataSetId: Int
)