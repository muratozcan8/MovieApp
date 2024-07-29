package com.obss.firstapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.model.movie.MovieList
import com.obss.firstapp.network.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val movieApi: MovieApiService): ViewModel() {

    private val _popularMovieList = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovieList: StateFlow<List<Movie>> = _popularMovieList


    fun getPopularMovies() {
        viewModelScope.launch {
            val response = movieApi.getPopularMovies(1)
            _popularMovieList.update { response }
            Log.i("HomeViewModel", "Popular movies: $response")
        }
    }

}