package id.android.official.moviephile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [MoviesEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(MoviesTypeConverter::class)
abstract class MoviesDatabase: RoomDatabase() {

    abstract fun moviesDao(): MoviesDao
}