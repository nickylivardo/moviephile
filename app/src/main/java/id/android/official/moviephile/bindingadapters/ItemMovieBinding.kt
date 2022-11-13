package id.android.official.moviephile.bindingadapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import id.android.official.moviephile.R

class ItemMovieBinding {
    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_error_image_placeholder)
            }
        }

        @BindingAdapter("setMovieYear")
        @JvmStatic
        fun setMovieYear(textView: TextView, year: Int) {
            textView.text = year.toString()
        }

        @BindingAdapter("setMovieRank")
        @JvmStatic
        fun setMovieRank(textView: TextView, rank: Int) {
            textView.text = rank.toString()
        }
    }
}