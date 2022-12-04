package id.android.official.moviephile.models


import com.google.gson.annotations.SerializedName

data class PlotSummary(
    @SerializedName("text")
    val text: String?
)