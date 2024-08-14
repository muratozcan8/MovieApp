package com.obss.firstapp.ui.view.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obss.firstapp.data.local.FavoriteMovie
import com.obss.firstapp.data.repository.MovieRepository
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

        private val _errorMessage = MutableStateFlow("")
        val errorMessage: StateFlow<String> = _errorMessage

        fun getAllFavoriteMovies() {
            viewModelScope.launch {
                movieRepository.getFavoriteMovies(_favoriteMovies, _errorMessage)
            }
        }
    }
