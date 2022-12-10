package id.android.official.moviephile.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import id.android.official.moviephile.R
import id.android.official.moviephile.databinding.FragmentProfileBinding
import id.android.official.moviephile.ui.EditProfileActivity
import id.android.official.moviephile.ui.LoginActivity
import id.android.official.moviephile.ui.VerificationActivity
import id.android.official.moviephile.utils.Constants.Companion.SIGNUP_STATUS
import id.android.official.moviephile.viewmodels.FirebaseViewModel


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseViewModel: FirebaseViewModel

    private var userId : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnLogOut.setOnClickListener{
            showAlertDialogToLogOut()
        }

        userId = firebaseViewModel.auth.currentUser?.uid

        if(userId != null) {
            Log.d("USER_ID", userId!!)
            firebaseViewModel.getUserData(userId!!, this)
        }

        binding.btnProfileEdit.setOnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            intent.putExtra("userId",userId)
            startActivity(intent)
        }




        return root
    }


    private fun showAlertDialogToLogOut() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(resources.getString(R.string.log_out))
        builder.setMessage(resources.getString(R.string.sign_out_dialog_message))
        builder.setIcon(R.drawable.ic_warning)

        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->
            signOut()
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun assignUserData(userData: id.android.official.moviephile.models.UserData) {
        binding.profileName.text = userData.name
        binding.profileMobile.text = userData.mobile
        binding.profileQuote.text = ""
        loadImageFromUrl(binding.profileImage, userData.image)
        binding.profileQuote.text = userData.quote

    }

    private fun signOut(){
        firebaseViewModel.auth.signOut()

        firebaseViewModel.saveBooleanPreferences(SIGNUP_STATUS, false)

        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finishAffinity(requireActivity())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun loadImageFromUrl(imageView: ImageView, imageUrl: String?) {
        imageView.load(imageUrl) {
            crossfade(600)
            error(R.drawable.ic_user_place_holder)
            fallback(R.drawable.ic_user_place_holder)
        }
    }

}