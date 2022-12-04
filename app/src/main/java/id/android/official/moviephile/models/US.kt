package id.android.official.moviephile.models


import com.google.gson.annotations.SerializedName

data class US(
    @SerializedName("certificate")
    val certificate: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("ratingReason")
    val ratingReason: String?,
    @SerializedName("ratingsBody")
    val ratingsBody: String?
)