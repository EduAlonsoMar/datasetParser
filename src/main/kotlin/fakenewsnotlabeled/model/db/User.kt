package fakenewsnotlabeled.model.db

data class User(val id: Int, val userName: String, val followers: Int, val friends: Int) {
}