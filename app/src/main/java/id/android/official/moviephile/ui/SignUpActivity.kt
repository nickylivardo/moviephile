package id.android.official.moviephile.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import id.android.official.moviephile.R
import id.android.official.moviephile.databinding.ActivitySignUpBinding
import id.android.official.moviephile.utils.Constants
import id.android.official.moviephile.viewmodels.FirebaseViewModel
import kotlin.system.exitProcess

@AndroidEntryPoint
class SignUpActivity : BaseActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mNameTextField: TextInputEditText

    private lateinit var firebaseViewModel: FirebaseViewModel

    private var mobile : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseViewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]

        binding = ActivitySignUpBinding.inflate(layoutInflater)

        val configuration: Configuration = resources.configuration
        configuration.fontScale = 1f //0.85 small size, 1 normal size, 1,15 big etc
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        configuration.densityDpi = resources.displayMetrics.xdpi.toInt()
        baseContext.resources.updateConfiguration(configuration, metrics)

        setContentView(binding.root)

        mNameTextField = binding.nameField


        mobile = firebaseViewModel.readPreferences(Constants.MOBILE_NUMBER)

        binding.btnSignUp.setOnClickListener{
            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
            if(validateSignInDetails()) {
                showProgressDialog(resources.getString(R.string.wait))
                firebaseViewModel.onSignUp(mobile, mNameTextField.text.toString(), "", this)
            }


        }

        onBackPressedDispatcher.addCallback(this /* lifecycle owner */, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Back is pressed... Close The App
                exitProcess(0)
            }
        })

    }


    fun signUpSuccess(){
        hideProgressDialog()
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }





    private fun validateSignInDetails(): Boolean {
        return when {
            TextUtils.isEmpty(mNameTextField.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_name), true)
                false
            }
            else -> {
                true
            }
        }
    }

}