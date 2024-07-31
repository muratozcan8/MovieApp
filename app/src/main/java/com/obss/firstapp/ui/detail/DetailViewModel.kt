package com.obss.firstapp.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obss.firstapp.model.movieDetail.MovieDetail
import com.obss.firstapp.network.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val movieApi: MovieApiService): ViewModel() {

    private val _movie = MutableStateFlow<MovieDetail?>(null)
    val movie: StateFlow<MovieDetail?> = _movie

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = movieApi.getMovieDetails(movieId)
                _movie.value = response
            } catch (exception: Exception) {
                catchException(exception)
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