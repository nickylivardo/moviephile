package id.android.official.moviephile.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import id.android.official.moviephile.R
import id.android.official.moviephile.adapters.MoviesAdapter
import id.android.official.moviephile.databinding.FragmentHomeBinding
import id.android.official.moviephile.utils.Constants.Companion.API_HOST
import id.android.official.moviephile.utils.Constants.Companion.API_KEY
import id.android.official.moviephile.utils.NetworkListener
import id.android.official.moviephile.utils.NetworkResult
import id.android.official.moviephile.utils.observeOnce
import id.android.official.moviephile.viewmodels.MainViewModel
import id.android.official.moviephile.viewmodels.MoviesViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mShimmerFrameLayout: ShimmerFrameLayout
    private lateinit var mLinearLayout: LinearLayout
    private lateinit var mPopularRecyclerView: RecyclerView
    private val mAdapter = MoviesAdapter()

    private lateinit var networkListener: NetworkListener

    private lateinit var mainViewModel: MainViewModel
    private lateinit var moviesViewModel: MoviesViewModel


    //onCreate is created first than onCreateView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        moviesViewModel = ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        val root: View = binding.root

        setHasOptionsMenu(true)

        mShimmerFrameLayout = binding.shimmerRecyclerView
        mLinearLayout = binding.contentLayout
        mPopularRecyclerView = binding.rvPopular


        setupRecycleViewAdapter()
        readDatabase()

        lifecycleScope.launch{
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect {
                        status ->
                    Log.d("NetworkListener", status.toString())
                    moviesViewModel.networkStatus = status
                    moviesViewModel.showNetworkStatus()
                }
        }




        return root


    }

    private fun requestApiData() {
        Log.d("HomeFragment", "requestApiData called!")
        mainViewModel.getMovies(moviesViewModel.applyQueries(), API_KEY, API_HOST)
        mainViewModel.moviesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    mLinearLayout.visibility = View.VISIBLE
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
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

    private fun loadDataFromCache(){
        lifecycleScope.launch {
            mainViewModel.readMovies.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()){
                    mAdapter.setData(database[0].movie)
                    hideShimmerEffect()
                    mLinearLayout.visibility = View.VISIBLE
                } else {
                    hideShimmerEffect()
                }
            }
        }
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readMovies.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    Log.d("HomeFragment", "readDatabase called!")
                    mAdapter.setData(database[0].movie)
                    hideShimmerEffect()
                    mLinearLayout.visibility = View.VISIBLE
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun setupRecycleViewAdapter() {
        mPopularRecyclerView.adapter = mAdapter
        mPopularRecyclerView.layoutManager =
            GridLayoutManager(activity, 2)
        mPopularRecyclerView.setHasFixedSize(true)
        showShimmerEffect()
    }


    private fun showShimmerEffect() {
        mShimmerFrameLayout.startShimmer()
    }

    private fun hideShimmerEffect() {
        mShimmerFrameLayout.hideShimmer()
        mShimmerFrameLayout.visibility = View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.movies_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }
}