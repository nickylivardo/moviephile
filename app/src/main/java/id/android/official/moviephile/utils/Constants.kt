package id.android.official.moviephile.utils

import id.android.official.moviephile.utils.Credentials.Companion.MY_API_KEY

class Constants {

    companion object {
        const val API_KEY = MY_API_KEY
        const val API_HOST = "online-movie-database.p.rapidapi.com"
        const val API_KEY_HEADER_NAME = "X-RapidAPI-Key"
        const val API_HOST_HEADER_NAME = "X-RapidAPI-Host"
        const val BASE_URL = "https://online-movie-database.p.rapidapi.com"

        // API Query Keys
        const val QUERY_Q = "q"
        const val QUERY_TCONST = "tconst"
    }
}