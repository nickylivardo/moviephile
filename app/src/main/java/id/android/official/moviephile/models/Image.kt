package id.android.official.moviephile.models


import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("url")
    val url: String?,
)