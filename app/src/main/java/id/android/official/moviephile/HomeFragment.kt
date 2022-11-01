package id.android.official.moviephile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.shimmer.ShimmerFrameLayout
import id.android.official.moviephile.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mShimmerFrameLayout: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =  FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mShimmerFrameLayout = binding.shimmerRecyclerView

        mShimmerFrameLayout.startShimmer()

        return root



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}