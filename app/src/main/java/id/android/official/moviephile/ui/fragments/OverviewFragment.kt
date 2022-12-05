package id.android.official.moviephile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import id.android.official.moviephile.R
import id.android.official.moviephile.databinding.FragmentOverviewBinding
import id.android.official.moviephile.models.D
import id.android.official.moviephile.utils.Constants
import id.android.official.moviephile.utils.NetworkResult
import id.android.official.moviephile.viewmodels.MainViewModel
import id.android.official.moviephile.viewmodels.MoviesViewModel

@AndroidEntryPoint
class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var moviesViewModel: MoviesViewModel

    private lateinit var mShimmerFrameLayout: ShimmerFrameLayout
    private lateinit var mScrollLayout: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        moviesViewModel = ViewModelProvider(requireActivity())[MoviesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        mShimmerFrameLayout = binding.shimmerOverviewLayout
        mScrollLayout = binding.scrollContentLayout

        val args = arguments
        val myBundle : D? = args?.getParcelable("movieBundle")

        if(myBundle?.i?.imageUrl.isNullOrEmpty()) {
            binding.mainImageView.load(R.drawable.ic_error_image_placeholder)
        } else {
            binding.mainImageView.load(myBundle?.i?.imageUrl)
        }
        binding.titleTextView.text = myBundle?.l
        binding.typeDescription.text = myBundle?.q ?: "unknown"
        binding.rankValue.text = myBundle?.rank?.toString() ?: "[no data]"
        binding.starValue.text = myBundle?.s ?: "[no data]"

        getMovieDetailsApi(myBundle?.id.toString())

        return binding.root
    }


    private fun getMovieDetailsApi(idMovie: String) {
        showShimmerEffect()
        mainViewModel.getMovieDetails(moviesViewModel.applySearchMovieDetails(idMovie),
            Constants.API_KEY,
            Constants.API_HOST
        )
        mainViewModel.movieDetailsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val movies = response.data
                    movies?.let {
                        binding.summaryTextView.text = it.plotSummary?.text ?: "[no data]"
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        }
    }

    private fun showShimmerEffect() {
        mShimmerFrameLayout.startShimmer()
    }

    private fun hideShimmerEffect() {
        mShimmerFrameLayout.stopShimmer()
        mShimmerFrameLayout.visibility = View.GONE
        mScrollLayout.visibility = View.VISIBLE
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}