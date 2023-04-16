package data.database.model

class ErrorForNotLabeled(
    val configurationId: Int,
    val datasetNotLabeledId: Int,
    val rmse: Double,
    val nrmse: Double
) {
    var id: Int? = null
}