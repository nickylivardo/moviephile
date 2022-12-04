package id.android.official.moviephile.models


import com.google.gson.annotations.SerializedName

data class Ratings(
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("ratingCount")
    val ratingCount: Int?,
    @SerializedName("topRank")
    val topRank: Int?
)