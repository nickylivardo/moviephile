package id.android.official.moviephile

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.android.official.moviephile.data.Repository
import id.android.official.moviephile.models.Movie
import id.android.official.moviephile.utils.NetworkResult
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    var moviesResponse: MutableLiveData<NetworkResult<Movie>> = MutableLiveData()

    fun getMovies(queries: Map<String, String>, api_key: String, api_host: String) = viewModelScope.launch {
        getRecipesSafeCall(queries, api_key, api_host)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>, apiKey: String, apiHost: String) {
        moviesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try {
                val response = repository.remote.getMovies(queries, apiKey, apiHost)
                moviesResponse.value = handleMoviesResponse(response)
            } catch (e: Exception) {
                moviesResponse.value = NetworkResult.Error("Catch Exception!!!")
            }
        } else {
            moviesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun handleMoviesResponse(response: Response<Movie>): NetworkResult<Movie>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.d.isNullOrEmpty() -> {
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