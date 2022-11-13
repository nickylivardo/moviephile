package id.android.official.moviephile.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import id.android.official.moviephile.models.Movie
import id.android.official.moviephile.utils.Constants.Companion.MOVIES_TABLE

@Entity(tableName = MOVIES_TABLE)
class MoviesEntity(
    var movie: Movie
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}