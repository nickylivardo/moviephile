package id.android.official.moviephile.models


import com.google.gson.annotations.SerializedName

data class Certificates(
    @SerializedName("US")
    val uS: List<US>?
)