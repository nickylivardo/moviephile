package id.android.official.moviephile.data

import id.android.official.moviephile.data.network.MoviesApi
import id.android.official.moviephile.models.Movie
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val moviesApi: MoviesApi
) {
    suspend fun getMovies(queries: Map<String, String>, api_key: String, api_host: String): Response<Movie> {
        return moviesApi.getMovies(queries, api_key, api_host)
    }

    suspend fun searchMovies(searchQuery: Map<String, String>, api_key: String, api_host: String): Response<Movie> {
        return moviesApi.searchMovies(searchQuery, api_key, api_host)
    }

}