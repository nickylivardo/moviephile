package id.android.official.moviephile.ui

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import dagger.hilt.android.AndroidEntryPoint
import id.android.official.moviephile.R
import id.android.official.moviephile.databinding.ActivityVerificationBinding
import id.android.official.moviephile.utils.Constants.Companion.MOBILE_NUMBER
import id.android.official.moviephile.utils.SMSReceiver
import id.android.official.moviephile.viewmodels.FirebaseViewModel
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class VerificationActivity : BaseActivity() {

    private var intentFilter: IntentFilter? = null
    private var smsReceiver: SMSReceiver? = null

    private lateinit var binding: ActivityVerificationBinding
    private lateinit var firebaseViewModel: FirebaseViewModel

    var counter = 60
    private var mobile : String = ""

    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    lateinit var storedVerificationId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseViewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]

        val configuration: Configuration = resources.configuration
        configuration.fontScale = 1.toFloat() //0.85 small size, 1 normal size, 1,15 big etc

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        configuration.densityDpi = resources.displayMetrics.xdpi.toInt()
        baseContext.resources.updateConfiguration(configuration, metrics)

        binding = ActivityVerificationBinding.inflate(layoutInflater)

        setContentView(binding.root)


        binding.btnResendOtp.isEnabled = false
        startTimeCounter()

        auth=FirebaseAuth.getInstance()
        auth.setLanguageCode("id")

        mobile = intent.getStringExtra("storedPhoneNumber").toString().trim { it <= ' ' }


        binding.btnVerify.setOnClickListener{
            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }

            val otp = binding.otpField.text?.trim().toString()
            if(otp.isNotEmpty()){
                if(this::storedVerificationId.isInitialized){
                    showProgressDialog(resources.getString(R.string.please_wait))

                    val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        storedVerificationId, otp)
                    signInWithPhoneAuthCredential(credential)
                }
                else{
                    Toast.makeText(this,"Check Your Phone Number and Try Again Later", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Enter OTP", Toast.LENGTH_SHORT).show()
            }

//            val intent = Intent(applicationContext, SignUpActivity::class.java)
//            startActivity(intent)
        }


        binding.btnResendOtp.setOnClickListener {
            if(this::resendToken.isInitialized){
                resendOTP(mobile)
                binding.btnResendOtp.isEnabled = false
                startTimeCounter()
                showErrorSnackBar(resources.getString(R.string.otp_resend_success), false)
            }
            else{
                Toast.makeText(this,"OTP gagal dikirim", Toast.LENGTH_SHORT).show()
            }
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            // This method is called when the verification is completed
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                showProgressDialog(resources.getString(R.string.please_wait))
                Log.d("OK" , "onVerificationCompletedSuccess")
                signInWithPhoneAuthCredential(credential)
            }
            // Called when verification is failed add log statement to see the exception
            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("FAIL" , "onVerificationFailed  $e")
                e.localizedMessage?.let { showErrorSnackBar(it, true) }
            }
            // On code is sent by the firebase this method is called
            // in here we start a new activity where user can enter the OTP
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("OK","onCodeSent: $verificationId")
                storedVerificationId = verificationId
                resendToken = token
                // Start a new activity using intent
                // also send the storedVerificationId using intent
                // we will use this id to send the otp back to firebase
            }
        }

        sendVerificationCode(mobile)

        initSmsListener()
        initBroadCast()


    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    firebaseViewModel.checkUserRegistration(this)
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this,"OTP Tidak Valid", Toast.LENGTH_SHORT).show()
                        hideProgressDialog()
                    }
                }
            }
    }


    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun resendOTP(number: String){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(resendToken) // ForceResendingToken from callbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun userLoggedInSuccess() {
        hideProgressDialog()

        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    fun userNewLoggedInSuccess() {
        hideProgressDialog()

        firebaseViewModel.savePreferences(MOBILE_NUMBER, mobile)

        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun initBroadCast() {
        intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        smsReceiver = SMSReceiver()
        smsReceiver?.setOTPListener(object : SMSReceiver.OTPReceiveListener {
            override fun onOTPReceived(otp1: String?) {
                if(this@VerificationActivity::storedVerificationId.isInitialized){
                    showToast("OTP Received: $otp1")
                    binding.otpField.setText("$otp1")
                    val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        storedVerificationId, otp1.toString())
                }
                else{
                    showToast("VERIFICATIONID NOT INITIALIZED OTP Received: $otp1")
                }
            }
        })
    }

    private fun initSmsListener() {
        val client = SmsRetriever.getClient(this)
        client.startSmsRetriever()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(smsReceiver, intentFilter)
    }

    private fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(smsReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        smsReceiver = null
    }




    fun startTimeCounter() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.btnResendOtp.text = "Kirim ulang OTP (${counter.toString()})"
                counter--
            }
            override fun onFinish() {
                binding.btnResendOtp.text = "Kirim ulang OTP"
                counter = 60
                binding.btnResendOtp.isEnabled = true
            }
        }.start()
    }
}