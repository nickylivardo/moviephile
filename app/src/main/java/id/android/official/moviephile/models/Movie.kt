package id.android.official.moviephile.models


import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("d")
    val d: List<D>,
    @SerializedName("q")
    val q: String?
)