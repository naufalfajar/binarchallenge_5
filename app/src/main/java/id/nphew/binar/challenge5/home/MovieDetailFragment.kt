package id.nphew.binar.challenge5.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import id.nphew.binar.challenge5.R
import id.nphew.binar.challenge5.databinding.FragmentMovieDetailBinding
import id.nphew.binar.challenge5.repository.viewModelsFactory
import id.nphew.binar.challenge5.service.ApiClient
import id.nphew.binar.challenge5.service.ApiService
import id.nphew.binar.challenge5.viewmodel.MovieViewModel

class MovieDetailFragment : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() =_binding!!

    private val apiService: ApiService by lazy { ApiClient.instance }
    private val viewModel: MovieViewModel by viewModelsFactory { MovieViewModel(apiService) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = MovieDetailFragmentArgs.fromBundle(arguments as Bundle).movieId
        viewModel.getDetailMovie(movieId!!)
        observeData()
    }

    private fun observeData() {
        viewModel.detailSuccess.observe(viewLifecycleOwner) {
            binding.apply {
//                Glide.with(requireContext())
//                    .load("https://www.themoviedb.org/t/p/w220_and_h330_face/" + it.backdropPath)
//                    .into(ivBackdrop)
                Glide.with(requireContext())
                    .load("https://www.themoviedb.org/t/p/w220_and_h330_face/" + it.posterPath)
                    .into(ivPoster)
                tvReleaseDate.text = it.releaseDate
                tvTitle.text = it.title
                tvOverview.text = it.overview
                pbMovie.isVisible = false
            }
        }
        viewModel.dataError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }
}