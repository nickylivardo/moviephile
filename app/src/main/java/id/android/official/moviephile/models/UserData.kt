package id.android.official.moviephile.models

data class UserData(
    val UserID: String? = null,
    val name: String? = null,
    val mobile: String? = null,
    val image: String? = null,
    val following: List<String>? = null
) {
    fun toMap() = mapOf(
        "userID" to UserID,
        "name" to name,
        "mobile" to mobile,
        "image" to image,
        "following" to following
    )
}
