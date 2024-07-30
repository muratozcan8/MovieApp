package com.obss.firstapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obss.firstapp.model.movie.MovieList
import com.obss.firstapp.network.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val movieApi: MovieApiService): ViewModel() {

    private val _popularMovieList = MutableStateFlow(MovieList(emptyList()))
    val popularMovieList: StateFlow<MovieList> = _popularMovieList

    private val _topRatedMovieList = MutableStateFlow(MovieList(emptyList()))
    val topRatedMovieList: StateFlow<MovieList> = _popularMovieList

    private val _loadingStateFlow = MutableStateFlow(false)
    val loadingStateFlow: StateFlow<Boolean> = _loadingStateFlow


    fun getPopularMovies() {
        _loadingStateFlow.update { true }
        viewModelScope.launch {
            try {
                val response = movieApi.getPopularMovies(1)
                _popularMovieList.update { response }
            } catch (exception: Exception) {
                catchException(exception)
            } finally {
                _loadingStateFlow.update { false }
            }

        }
    }

    fun getTopRatedMovies() {
        _loadingStateFlow.update { true }
        viewModelScope.launch {
            try {
                val response = movieApi.getTopRatedMovies(1)
                _topRatedMovieList.update { response }
            } catch (exception: Exception) {
                catchException(exception)
            } finally {
                _loadingStateFlow.update { false }
            }

        }
    }

    private fun catchException(exception: Exception) {
        when (exception) {
            is UnknownHostException -> {
                Log.e("network exception", "no network")
            }
            is HttpException -> {
                Log.e("network exception", "HTTP exception")
            }
            else -> Log.e("network exception", "unknown", exception)
        }
    }

}