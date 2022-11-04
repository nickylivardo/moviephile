package id.android.official.moviephile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.android.official.moviephile.databinding.ItemMovieLayoutBinding
import id.android.official.moviephile.models.D
import id.android.official.moviephile.models.Movie
import id.android.official.moviephile.utils.MoviesDiffUtil

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MyViewHolder>() {

    private var movies = emptyList<D>()

    class MyViewHolder(private val binding: ItemMovieLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(d: D) {
            binding.movie = d
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMovieLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentMovie = movies[position]
        holder.bind(currentMovie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setData(newData: Movie) {
        val moviesDiffUtil = MoviesDiffUtil(movies, newData.d)
        val diffUtilResult = DiffUtil.calculateDiff(moviesDiffUtil)
        movies = newData.d
        diffUtilResult.dispatchUpdatesTo(this)

    }
}