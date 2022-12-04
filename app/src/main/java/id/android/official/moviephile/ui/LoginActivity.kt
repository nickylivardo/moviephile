package id.android.official.moviephile.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import id.android.official.moviephile.R
import id.android.official.moviephile.databinding.ActivityLoginBinding
import id.android.official.moviephile.viewmodels.FirebaseViewModel

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mPhoneTextField: TextInputEditText

    private lateinit var firebaseViewModel: FirebaseViewModel

    private var mobile: String = ""
    var counter = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        val configuration: Configuration = resources.configuration
        configuration.fontScale = 1f //0.85 small size, 1 normal size, 1,15 big etc
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        configuration.densityDpi = resources.displayMetrics.xdpi.toInt()
        baseContext.resources.updateConfiguration(configuration, metrics)

        setContentView(
            binding.root
        )

        firebaseViewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]

        mPhoneTextField = binding.phoneNumber

        binding.btnLogIn.setOnClickListener{

            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
            mobile =mPhoneTextField.text.toString().trim { it <= ' ' }
            if(validateSignInDetails()){
                startTimeCounter()
                mobile = "+62$mobile"
                val intent = Intent(applicationContext, VerificationActivity::class.java)
                intent.putExtra("storedPhoneNumber",mobile)
                startActivity(intent)
            }
        }

    }


    private fun validateSignInDetails(): Boolean {
        return when {
            TextUtils.isEmpty(mPhoneTextField.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.please_fill), true)
                false
            }
            mPhoneTextField.length() <= 5 -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_phone_number_length), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun startTimeCounter() {
        val text = getString(R.string.login)
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.btnLogIn.isEnabled = false
                binding.btnLogIn.text = "$text (${counter.toString()})"
                counter--
            }
            override fun onFinish() {
                counter = 60
                binding.btnLogIn.isEnabled = true
                binding.btnLogIn.text = text
            }
        }.start()
    }

}