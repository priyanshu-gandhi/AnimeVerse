package com.example.animeverse.presentation.home

import android.util.Log
import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeverse.domain.Anime
import com.example.animeverse.domain.AnimePage
import com.example.animeverse.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    private val TAG = "HomeFragmentDebug"
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var currentPage = 1

    private var isFetching: Boolean = false

    private var isLastPage : Boolean = false

    private val itemCount = MutableStateFlow(20)
    private val searchQuery = MutableStateFlow("")

    val anime: StateFlow<List<Anime>> = searchQuery
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                itemCount.flatMapLatest { count ->
                    homeRepository.getTopAnime(1, count)
                }
            } else {
                homeRepository.searchAnime(query)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getTopAnime() {
        if (isFetching || isLastPage) return

        viewModelScope.launch {
            _isLoading.value = true
            isFetching = true

            // FIX: Increment counts BEFORE the network call
            // This ensures the UI asks Room for more data even if offline
            currentPage++
            itemCount.value = currentPage * 20

            try {
                homeRepository.syncTopAnime(currentPage - 1)
            } catch (e: Exception) {
                Log.e(TAG, "Sync Failed (Offline Mode): ${e.message}")
                // We don't return here; the 'itemCount' already increased,
                // so Room will emit the next 20 cached items!
            } finally {
                isFetching = false
                _isLoading.value = false
            }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        searchQuery.value = newQuery
        if (newQuery.isNotEmpty()) {
            syncSearch(newQuery)
        }
    }

    private fun syncSearch(query: String) {
        viewModelScope.launch {
            try {
                homeRepository.syncSearchAnime(query)
            } catch (e: Exception) {
                Log.e("Search", "Offline search mode")
            }
        }
    }

}