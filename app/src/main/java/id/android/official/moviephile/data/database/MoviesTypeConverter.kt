package id.android.official.moviephile.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.android.official.moviephile.models.Movie

class MoviesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun moviesToString(movie: Movie): String {
        return gson.toJson(movie)
    }

    @TypeConverter
    fun stringToMovie(data: String): Movie {
        val listType = object : TypeToken<Movie>() {}.type
        return gson.fromJson(data, listType)
    }
}