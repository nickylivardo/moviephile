package id.android.official.moviephile.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import id.android.official.moviephile.utils.Constants.Companion.QUERY_Q

class MoviesViewModel(application: Application) : AndroidViewModel(application) {

     fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_Q] = "Avengers"

        return queries
    }
}