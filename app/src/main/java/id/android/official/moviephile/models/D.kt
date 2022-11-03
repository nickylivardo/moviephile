package id.android.official.moviephile.models


import com.google.gson.annotations.SerializedName

data class D(
    @SerializedName("i")
    val i: I,
    @SerializedName("id")
    val id: String,
    @SerializedName("l")
    val l: String,
    @SerializedName("q")
    val q: String,
    @SerializedName("qid")
    val qid: String,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("s")
    val s: String,
    @SerializedName("y")
    val y: Int,
    @SerializedName("yr")
    val yr: String
)