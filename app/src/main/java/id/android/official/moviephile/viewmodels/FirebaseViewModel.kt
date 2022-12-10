package id.android.official.moviephile.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import id.android.official.moviephile.data.DataStoreRepository
import id.android.official.moviephile.models.UserData
import id.android.official.moviephile.ui.SignUpActivity
import id.android.official.moviephile.ui.VerificationActivity
import id.android.official.moviephile.ui.fragments.ProfileFragment
import id.android.official.moviephile.utils.Constants.Companion.SIGNUP_STATUS
import id.android.official.moviephile.utils.Constants.Companion.USERS
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val readSignUpPreferences = dataStoreRepository.readSignUpStatusBoolean

    val readMobilePreferences = dataStoreRepository.readMobileNumber

    private var userData : UserData? = null


    private fun getCurrentUserID(): String {
        val currentUser = auth.currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun onSignUp(phoneNumber: String, name: String, imageUrl: String?, quote: String?, activity: Activity) {
        createOrUpdateProfile(name, phoneNumber, imageUrl, quote, activity)
    }


    private fun createOrUpdateProfile(
        name: String?,
        phoneNumber: String?,
        imageUrl: String?,
        quote: String?,
        activity: Activity
    ) {
        val uid = getCurrentUserID()
        val userData = UserData(
            userID = uid,
            name = name,
            mobile = phoneNumber,
            image = imageUrl,
            quote = quote,
            following = userData?.following
        )

        uid.let {   uid ->
            db.collection(USERS).document(uid).get().addOnSuccessListener {
                if(it.exists()) {
                    it.reference.update(userData.toMap())
                        .addOnSuccessListener {
                            this.userData = userData
                            Log.d("Firebase", "Success Update UserData")
                        }
                        .addOnFailureListener {
                            Log.d("Firebase", "Error Update UserData")
                        }
                } else {
                    db.collection(USERS).document(uid).set(userData)
                    saveBooleanPreferences(SIGNUP_STATUS, true)
//                    getUserData(uid)
                    when (activity) {
                        is SignUpActivity -> {
                            activity.signUpSuccess()
                        }
                    }
                }
            }

        }
    }

    fun getUserData (uid: String, fragment: ProfileFragment) {
        db.collection(USERS).document(uid).get().addOnSuccessListener{
            val userData = it.toObject(UserData::class.java)!!
            fragment.assignUserData(userData)
        }
    }

    fun checkUserRegistration(activity: Activity) {
        db.collection(USERS).document(getCurrentUserID()).get().addOnSuccessListener{
            if(it.exists()) {
                Log.d("USER", "USER already exists.")
                saveBooleanPreferences2(SIGNUP_STATUS, true, activity)
//                when (activity){
//                    is VerificationActivity -> {
//                        activity.userLoggedInSuccess()
//                    }
//                }
            }else {
                Log.d("USER", "New User Logged In")
                when (activity){
                    is VerificationActivity -> {
                        activity.userNewLoggedInSuccess()
                    }
                }
            }
        }
    }



    fun savePreferences(key: String, value: String) {
        viewModelScope.launch {
            dataStoreRepository.savePreferences(key, value)
        }
    }

//    fun readPreferences(key: String) : String {
//        var preferences: String? = ""
//        viewModelScope.launch {
//            preferences = dataStoreRepository.readPreferences(key)
//            Log.d("PREFERENCES", preferences.toString())
//        }
//        return preferences!!
//    }

    fun saveBooleanPreferences(key: String, value: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.saveBooleanPreferences(key, value)
        }
    }

    private fun saveBooleanPreferences2(key: String, value: Boolean, activity: Activity) {
        viewModelScope.launch {
            dataStoreRepository.saveBooleanPreferences(key, value)
            when (activity){
                is VerificationActivity -> {
                    activity.userLoggedInSuccess()
                }
            }
        }
    }

//    fun readBooleanPreferences(key: String) : Boolean {
//        var preferences: Boolean? = false
//        viewModelScope.launch {
//            preferences = dataStoreRepository.readBooleanPreferences(key)
//            Log.d("PREFERENCES", "SIGNUP STATUS IS"+preferences.toString())
//        }
//        return preferences!!
//    }












}