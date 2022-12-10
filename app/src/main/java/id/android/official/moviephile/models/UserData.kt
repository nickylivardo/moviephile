package id.android.official.moviephile.models

data class UserData(
    val userID: String? = null,
    val name: String? = null,
    val mobile: String? = null,
    val image: String? = null,
    val quote: String? = null,
    val following: List<String>? = null
) {
    fun toMap() = mapOf(
        "userID" to userID,
        "name" to name,
        "mobile" to mobile,
        "image" to image,
        "quote" to quote,
        "following" to following
    )
}
