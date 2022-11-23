package id.android.official.moviephile.data.network

import id.android.official.moviephile.models.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface MoviesApi {

    @GET("auto-complete")
    suspend fun getMovies(
        @QueryMap queries: Map<String, String>,
        @Header("X-RapidAPI-Key") api_key: String,
        @Header("X-RapidAPI-Host") api_host: String
    ): Response<Movie>

    @GET("auto-complete")
    suspend fun searchMovies(
        @QueryMap searchQuery: Map<String, String>,
        @Header("X-RapidAPI-Key") api_key: String,
        @Header("X-RapidAPI-Host") api_host: String
    ): Response<Movie>
}