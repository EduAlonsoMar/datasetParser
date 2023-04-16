package data.database.model

data class ErrorForLabeled(
    val id: Int? = null,
    val configId: Int? = null,
    val DataSetLabeledId: Int? = null,
    val rmseBelievers: Double? = null,
    val rmseDeniers: Double? = null,
    val nmseBelievers: Double? = null,
    val nmseDeniers: Double? = null,
    val rmseTotal: Double? = null
) {
}