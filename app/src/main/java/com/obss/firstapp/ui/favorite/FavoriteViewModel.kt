package com.obss.firstapp.ui.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obss.firstapp.repository.MovieRepository
import com.obss.firstapp.room.FavoriteMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) : ViewModel() {
        private var _favoriteMovies = MutableStateFlow<List<FavoriteMovie>>(listOf())
        val favoriteMovies: StateFlow<List<FavoriteMovie>> = _favoriteMovies

        fun getAllFavoriteMovies() {
            viewModelScope.launch {
                try {
                    val response = movieRepository.getFavoriteMovies()
                    _favoriteMovies.value = response
                } catch (exception: Exception) {
                    Log.e("Exception", exception.toString())
                }
            }
        }
    }
