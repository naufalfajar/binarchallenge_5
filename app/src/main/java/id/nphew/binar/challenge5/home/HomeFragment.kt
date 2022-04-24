package id.nphew.binar.challenge5.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import id.nphew.binar.challenge5.R
import id.nphew.binar.challenge5.adapter.MovieAdapter
import id.nphew.binar.challenge5.database.AccountDatabase
import id.nphew.binar.challenge5.databinding.FragmentHomeBinding
import id.nphew.binar.challenge5.model.MoviePopularItem
import id.nphew.binar.challenge5.repository.AccountRepo
import id.nphew.binar.challenge5.repository.viewModelsFactory
import id.nphew.binar.challenge5.service.ApiClient
import id.nphew.binar.challenge5.service.ApiService
import id.nphew.binar.challenge5.sharedPrefs.SharedPrefs
import id.nphew.binar.challenge5.viewmodel.MovieViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var authenticator: SharedPrefs
    private var accountDb: AccountDatabase? = null
    private var accEmail: String? = null

    private val apiService: ApiService by lazy { ApiClient.instance }
    private val accountRepo: AccountRepo by lazy { AccountRepo(requireContext()) }
    private val viewModel: MovieViewModel by viewModelsFactory { MovieViewModel(apiService)}

    private lateinit var movieAdapter: MovieAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountDb = AccountDatabase.getInstance(requireContext())
        authenticator = SharedPrefs(requireContext())
        accEmail = authenticator.loggedInEmail()

        initRecyclerView()
        viewModel.getAllMovies()
        observeData()

        setUsername(accEmail)
        moveToProfile()
    }
    private fun initRecyclerView() {
//        movieAdapter = MovieAdapter()
        movieAdapter = MovieAdapter { id: Int, movie: MoviePopularItem ->
//            val bundle = Bundle()
//            bundle.putInt("id", id)
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(id))
        }
        binding.rvMovie.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun observeData() {
        viewModel.dataError.observe(requireActivity()) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.movie.observe(viewLifecycleOwner) {
            movieAdapter.updateData(it.results)
        }

    }
    private fun setUsername(email: String?){
        CoroutineScope(Dispatchers.Main).launch {
            val welcome = "Welcome, "
            val caution = "!"
            val username = getUsername(email)
            val value = welcome + username + caution
            binding.tvHomeWelcome.text = value
        }
    }

    private suspend fun getUsername(email: String?): String? = withContext(Dispatchers.IO) {
        return@withContext accountDb?.accountDao()?.getUsername(email)
    }

    private fun moveToProfile(){
        binding.ivHomeProfile.setOnClickListener{
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())
        }
    }


}