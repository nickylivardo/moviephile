package id.android.official.moviephile.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.android.official.moviephile.data.Repository
import id.android.official.moviephile.data.database.MoviesEntity
import id.android.official.moviephile.models.Movie
import id.android.official.moviephile.utils.Constants
import id.android.official.moviephile.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {


    /** ROOM Database */

    val readMovies: LiveData<List<MoviesEntity>> = repository.local.readDatabase().asLiveData()

    private fun insertMovies(moviesEntity: MoviesEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertMovies(moviesEntity)
    }

    /** Retrofit */

    var moviesResponse: MutableLiveData<NetworkResult<Movie>> = MutableLiveData()
    var searchedMoviesResponse: MutableLiveData<NetworkResult<Movie>> = MutableLiveData()
    var searchedQuery : String = ""

    fun getMovies(queries: Map<String, String>, api_key: String, api_host: String) = viewModelScope.launch {
        getMoviesSafeCall(queries, api_key, api_host)
    }

    fun searchMovies(searchQuery: Map<String, String>, api_key: String, api_host: String) = viewModelScope.launch {
        searchMoviesSafeCall(searchQuery, api_key, api_host)
    }

    private suspend fun getMoviesSafeCall(queries: Map<String, String>, apiKey: String, apiHost: String) {
        moviesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try {
                val response = repository.remote.getMovies(queries, apiKey, apiHost)
                moviesResponse.value = handleMoviesResponse(response)

                val movie = moviesResponse.value!!.data
                if(movie != null) {
                    offlineCacheMovies(movie)
                }

            } catch (e: Exception) {
                moviesResponse.value = NetworkResult.Error("Catch Exception!!!")
            }
        } else {
            moviesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun searchMoviesSafeCall(searchQuery: Map<String, String>, apiKey: String, apiHost: String) {
        searchedMoviesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try {
                val response = repository.remote.searchMovies(searchQuery, apiKey, apiHost)
                searchedMoviesResponse.value = handleMoviesResponse(response)
                searchedQuery = searchQuery.getValue(Constants.QUERY_Q)
            } catch (e: Exception) {
                searchedMoviesResponse.value = NetworkResult.Error("Catch Exception!!!")
            }
        } else {
            searchedMoviesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun offlineCacheMovies(movie: Movie) {
        val moviesEntity = MoviesEntity(movie)
        insertMovies(moviesEntity)
    }

    private fun handleMoviesResponse(response: Response<Movie>): NetworkResult<Movie> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.d.isEmpty() -> {
                return NetworkResult.Error("Movies Not Found.")
            }
            response.isSuccessful -> {
                val movies = response.body()
                return NetworkResult.Success(movies!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}