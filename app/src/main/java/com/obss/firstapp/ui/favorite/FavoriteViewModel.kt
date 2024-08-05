package com.obss.firstapp.ui.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obss.firstapp.network.MovieApiService
import com.obss.firstapp.room.FavoriteMovie
import com.obss.firstapp.room.MovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val movieApi: MovieApiService,
                                            private val movieDao: MovieDao
): ViewModel() {

    private var _favoriteMovies = MutableStateFlow<List<FavoriteMovie>>(listOf())
    val favoriteMovies: StateFlow<List<FavoriteMovie>> = _favoriteMovies

    fun getAllFavoriteMovies() {
        viewModelScope.launch {
            try {
                val response = movieDao.getAllFavorites()
                _favoriteMovies.value = response
            } catch (exception: Exception) {
                Log.e("Exception", exception.toString())
            }
        }
    }
}