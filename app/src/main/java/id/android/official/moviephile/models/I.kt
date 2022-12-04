package id.android.official.moviephile.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class I(
    @SerializedName("imageUrl")
    val imageUrl: String?
) : Parcelable