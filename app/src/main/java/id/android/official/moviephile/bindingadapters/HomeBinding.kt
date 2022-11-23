package id.android.official.moviephile.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import id.android.official.moviephile.data.database.MoviesEntity
import id.android.official.moviephile.models.Movie
import id.android.official.moviephile.utils.NetworkResult

class HomeBinding {

    companion object {

        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibility (
            imageView: ImageView,
            apiResponse: NetworkResult<Movie>?,
            database: List<MoviesEntity>?
        ){
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                imageView.visibility = View.VISIBLE
            } else if (apiResponse is NetworkResult.Loading) {
                imageView.visibility = View.GONE
            } else if (apiResponse is NetworkResult.Success) {
                imageView.visibility = View.GONE
            }
        }

        @BindingAdapter("readApiResponse2", "readDatabase2", requireAll = true)
        @JvmStatic
        fun errorTextViewVisibility(
            textView: TextView,
            apiResponse: NetworkResult<Movie>?,
            database: List<MoviesEntity>?
        ){
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            } else if (apiResponse is NetworkResult.Loading) {
                textView.visibility = View.GONE
            } else if (apiResponse is NetworkResult.Success) {
                textView.visibility = View.GONE
            }
        }


        @BindingAdapter("readApiResponse3", "readSearchQuery", requireAll = true)
        @JvmStatic
        fun searchResultTextView(
            textView: TextView,
            apiResponse: NetworkResult<Movie>?,
            searchQuery: String
        ){
            if (apiResponse is NetworkResult.Success) {
                textView.text = "Result From \"$searchQuery\""
            }
        }
    }
}