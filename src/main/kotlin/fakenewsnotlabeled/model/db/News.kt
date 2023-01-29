package fakenewsnotlabeled.model.db

data class News (val index: Int, val title: String, val content: String, val isFake: Boolean) {
}