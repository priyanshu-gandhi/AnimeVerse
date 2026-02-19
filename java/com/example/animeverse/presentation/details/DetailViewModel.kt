package com.example.animeverse.presentation.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeverse.domain.AnimeDetails
import com.example.animeverse.domain.repository.DetailsRepository
import com.example.animeverse.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: DetailsRepository
) : ViewModel() {

    private var _animeDetails = MutableStateFlow<AnimeDetails?>(null)

    var animeDetails : StateFlow<AnimeDetails?> = _animeDetails.asStateFlow()


     fun getAnimeDetails(animeId: Int){
         viewModelScope.launch {
             try {
                 repository.getAnimeDetails(animeId).collect { cachedDetails ->
                     _animeDetails.value = cachedDetails
                 }
             } catch (e : Exception){
                 e.printStackTrace()
             }
         }
    }
}