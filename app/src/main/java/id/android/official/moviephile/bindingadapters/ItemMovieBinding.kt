package id.android.official.moviephile.bindingadapters

import android.content.res.Resources
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import id.android.official.moviephile.R

class ItemMovieBinding {
    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String?) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_error_image_placeholder)
                fallback(R.drawable.ic_error_image_placeholder)
            }
        }

        @BindingAdapter("setMovieYear")
        @JvmStatic
        fun setMovieYear(textView: TextView, year: Int?) {
            if (year != null) {
                textView.text = year.toString()
            } else {
                textView.text = Resources.getSystem().getString(R.string.no_data)
            }

        }

    }
}