package id.android.official.moviephile.models


import com.google.gson.annotations.SerializedName

data class Title(
    @SerializedName("id")
    val id: String?,
    @SerializedName("image")
    val image: Image?,
    @SerializedName("runningTimeInMinutes")
    val runningTimeInMinutes: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("titleType")
    val titleType: String?,
    @SerializedName("year")
    val year: Int?
)