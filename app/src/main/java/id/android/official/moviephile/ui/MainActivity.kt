package id.android.official.moviephile.ui

import android.content.Intent
import android.os.Bundle
import android.os.health.UidHealthStats
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgument
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import id.android.official.moviephile.R
import id.android.official.moviephile.databinding.ActivityMainBinding
import id.android.official.moviephile.viewmodels.FirebaseViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private lateinit var firebaseViewModel: FirebaseViewModel

    private var signUpStatus = false



    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseViewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]



        firebaseViewModel.readSignUpPreferences.asLiveData().observe(this) { value ->
            signUpStatus = value
            val loginStatus = firebaseViewModel.auth.currentUser
            Log.d("LOGIN", "USER IS LOGGED IN $loginStatus")
            Log.d("SIGNUP_STATUS", signUpStatus.toString())
            if(loginStatus == null) {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            } else {
                if (!signUpStatus) {
                    val intent = Intent(applicationContext, SignUpActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        val navController = navHostFragment.navController

        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.homeFragment, R.id.listFragment, R.id.profileFragment))

        binding.bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}