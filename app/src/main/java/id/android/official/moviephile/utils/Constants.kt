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

        // ROOM Database
        const val DATABASE_NAME = "movies_database"
        const val MOVIES_TABLE = "movies_table"

        // Firebase
        const val USERS = "users"
        const val USERID = "userId"

        const val MOVIE_LIST = "movie_list"

        //DataStore Preferences
        const val PREFERENCES_NAME = "my_preferences"
        const val SIGNUP_STATUS = "signup_status"
        const val MOBILE_NUMBER = "mobile_number"
        const val BACK_ONLINE = "back_online"
    }
}