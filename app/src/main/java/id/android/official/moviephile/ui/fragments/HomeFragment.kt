package id.android.official.moviephile.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import id.android.official.moviephile.viewmodels.MainViewModel
import id.android.official.moviephile.adapters.MoviesAdapter
import id.android.official.moviephile.databinding.FragmentHomeBinding
import id.android.official.moviephile.utils.Constants.Companion.API_HOST
import id.android.official.moviephile.utils.Constants.Companion.API_KEY
import id.android.official.moviephile.utils.NetworkResult

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mShimmerFrameLayout: ShimmerFrameLayout
    private lateinit var mScrollView: ScrollView
    private lateinit var mPopularRecyclerView: RecyclerView
    private val mAdapter = MoviesAdapter()
    private lateinit var mainViewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mShimmerFrameLayout = binding.shimmerRecyclerView
        mScrollView = binding.scrollLayout
        mPopularRecyclerView = binding.rvPopular
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        setupRecycleViewAdapter()
        requestApiData()
        return root


    }

    private fun requestApiData() {
        mainViewModel.getMovies(applyQueries(), API_KEY, API_HOST)
        mainViewModel.moviesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
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

    private fun setupRecycleViewAdapter() {
        mPopularRecyclerView.adapter = mAdapter
        mPopularRecyclerView.layoutManager =
            GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        mPopularRecyclerView.setHasFixedSize(true)
        showShimmerEffect()
    }


    private fun showShimmerEffect() {
        mShimmerFrameLayout.startShimmer()
    }

    private fun hideShimmerEffect() {
        mShimmerFrameLayout.hideShimmer()
        mShimmerFrameLayout.visibility = View.GONE
        mScrollView.visibility = View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}