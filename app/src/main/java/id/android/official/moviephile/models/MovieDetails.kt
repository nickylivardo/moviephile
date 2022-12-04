package id.android.official.moviephile.models


import com.google.gson.annotations.SerializedName

data class MovieDetails(
    @SerializedName("certificates")
    val certificates: Certificates?,
    @SerializedName("genres")
    val genres: List<String>?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("plotOutline")
    val plotOutline: PlotOutline?,
    @SerializedName("plotSummary")
    val plotSummary: PlotSummary?,
    @SerializedName("ratings")
    val ratings: Ratings?,
    @SerializedName("releaseDate")
    val releaseDate: String?,
    @SerializedName("title")
    val title: Title?
)