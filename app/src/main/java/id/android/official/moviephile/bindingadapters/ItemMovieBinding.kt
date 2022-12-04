package id.android.official.moviephile.bindingadapters

import android.content.res.Resources
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import id.android.official.moviephile.R
import id.android.official.moviephile.models.D
import id.android.official.moviephile.ui.fragments.HomeFragmentDirections

class ItemMovieBinding {
    companion object {

        @BindingAdapter("onMovieClickListener")
        @JvmStatic
        fun onMovieClickListener(itemMovieLayout: ConstraintLayout, movieDetails: D) {
            Log.d("onMovieClickListener", "CALLED")
            itemMovieLayout.setOnClickListener {
                try {
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailsActivity(movieDetails)
                    itemMovieLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.d("onMovieClickListener", e.toString())
                }
            }
        }

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
            textView.text = year.toString()
        }

    }
}