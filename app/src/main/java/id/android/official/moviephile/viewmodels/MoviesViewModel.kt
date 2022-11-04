package id.android.official.moviephile.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class MoviesViewModel(application: Application) : AndroidViewModel(application) {

     fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries["q"] = "Avengers"

        return queries
    }
}