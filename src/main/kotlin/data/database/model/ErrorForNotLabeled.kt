package data.database.model

data class ErrorForNotLabeled(
    val configurationId: Int,
    val datasetNotLabeledId: Int,
    val rmse: Double,
    val nrmse: Double,
    val rmseDays: Double,
    val nrmseDays: Double
) {
    var id: Int? = null
}

data class PairOfErrorsForNotLabeled(
    val rmse: Double,
    val nrmse: Double
)