package database.databaselabeled.model

import java.sql.Date

data class UserReaction (val id: Int, val date: Date, val userId: Int, val label: String, val dataSetId: Int)