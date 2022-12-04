package id.android.official.moviephile.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.android.official.moviephile.R
import id.android.official.moviephile.databinding.FragmentProfileBinding
import id.android.official.moviephile.ui.LoginActivity
import id.android.official.moviephile.utils.Constants.Companion.SIGNUP_STATUS
import id.android.official.moviephile.viewmodels.FirebaseViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseViewModel: FirebaseViewModel

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

        binding.btnLogOut.setOnClickListener{
            showAlertDialogToLogOut()
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

    private fun signOut(){
        firebaseViewModel.auth.signOut()

        firebaseViewModel.saveBooleanPreferences(SIGNUP_STATUS, false)

        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finishAffinity(requireActivity())
    }

}