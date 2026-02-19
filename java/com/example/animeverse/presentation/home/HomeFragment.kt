package com.example.animeverse.presentation.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animeverse.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment  : Fragment() {

    private val TAG = "HomeFragmentDebug"
    private var _binding: FragmentHomeBinding? = null
    private val  binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var animeAdapter : AnimeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Fragment started")
        setUpRecyclerView()
        observeViewModel()
        setUpWindowInsets()
        Log.d(TAG, "onViewCreated: Requesting top anime page 1")
        viewModel.getTopAnime()
        setUpSearch()
        registerNetworkCallback()
    }

    private fun setUpWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(0, systemBars.top, 0, 0)

            insets
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.anime.collect { animeList ->
                    val isQueryEmpty = binding.searchView.query.isNullOrEmpty()
                    Log.d(TAG, "observeViewModel: Received list of size ${animeList.size}")
                    if (animeList.isNotEmpty()) {
                        binding.progressBar.visibility = View.GONE
                        animeAdapter.submitList(animeList)
                    } else{
                        if (!isQueryEmpty){
                            binding.errorLayout.visibility = View.VISIBLE
                            binding.rvAnimeList.visibility = View.GONE
                            binding.txtError.text = "No anime found with this name"
                            binding.btnRetry.visibility = View.GONE
                        }
                        else{
                            binding.rvAnimeList.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun setUpInfiniteScroll() {
        binding.rvAnimeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy <= 0) return

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if ((visibleItemCount + firstVisibleItem)>= totalItemCount - 5 && firstVisibleItem >=0) {
                    viewModel.getTopAnime()
                }
            }
        })
    }

    private fun setUpSearch() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onSearchQueryChanged(newText ?: "")
                return true
            }
        })
    }
    private fun setUpRecyclerView() {
         animeAdapter = AnimeAdapter(onItemClick = { anime ->
             val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(anime.id)
             findNavController().navigate(action)
         }  )
         binding.rvAnimeList.apply {
             adapter = animeAdapter
             layoutManager = LinearLayoutManager(requireContext())
         }
        setUpInfiniteScroll()

    }

    private fun registerNetworkCallback() {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                    viewModel.getTopAnime()
                }
            }

            override fun onLost(network: Network) {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                    Snackbar.make(binding.root, "Connection lost. Using offline mode.", Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}