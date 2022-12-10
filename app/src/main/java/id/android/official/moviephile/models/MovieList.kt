package id.android.official.moviephile.models

data class MovieList(
    val userID: String? = null,
    val movieID: String? = null,
    val movieImage: String? = null,
    val movieTitle: String? = null,
    val movieYear: String? = null,
    val comment: String? = null,
    val review: String? = null
) {
    fun toMap() = mapOf(
        "userID" to userID,
        "movieID" to movieID,
        "movieImage" to movieImage,
        "movieTitle" to movieTitle,
        "movieYear" to movieYear,
        "comment" to comment,
        "review" to review
    )
}