package id.android.official.moviephile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import id.android.official.moviephile.R
import id.android.official.moviephile.databinding.FragmentOverviewBinding
import id.android.official.moviephile.models.D

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle : D? = args?.getParcelable("movieBundle")

        if(myBundle?.i?.imageUrl.isNullOrEmpty()) {
            binding.mainImageView.load(R.drawable.ic_error_image_placeholder)
        } else {
            binding.mainImageView.load(myBundle?.i?.imageUrl)
        }
        binding.titleTextView.text = myBundle?.l

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}