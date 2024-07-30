package com.obss.firstapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _popularMovieList = MutableStateFlow(MovieList(emptyList()))
    val popularMovieList: StateFlow<MovieList> = _popularMovieList

    private val _topRatedMovieList = MutableStateFlow(MovieList(emptyList()))
    val topRatedMovieList: StateFlow<MovieList> = _popularMovieList


    fun getPopularMovies() {
        viewModelScope.launch {
            val response = movieApi.getPopularMovies(2)
            _popularMovieList.update { response }
        }
    }

    fun getTopRatedMovies() {
        viewModelScope.launch {
            val response = movieApi.getTopRatedMovies(1)
            _topRatedMovieList.update { response }
        }
    }

}