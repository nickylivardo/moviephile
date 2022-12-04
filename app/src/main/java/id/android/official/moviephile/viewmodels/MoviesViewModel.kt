package id.android.official.moviephile.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.android.official.moviephile.data.DataStoreRepository
import id.android.official.moviephile.utils.Constants.Companion.BACK_ONLINE
import id.android.official.moviephile.utils.Constants.Companion.QUERY_Q
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    var networkStatus = false
    var backOnline = false

    val readBackOnlinePreferences = dataStoreRepository.readBackOnlineBoolean.asLiveData()

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_Q] = "Avengers"

        return queries
    }

    fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_Q] = searchQuery

        return queries
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "Tidak Ada Internet.", Toast.LENGTH_SHORT).show()
            saveBackOnline(BACK_ONLINE, true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "Back Online.", Toast.LENGTH_SHORT).show()
                saveBackOnline(BACK_ONLINE, false)
            }

        }
    }

    fun saveBackOnline (key: String, value: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBooleanPreferences(key, value)
        }
}