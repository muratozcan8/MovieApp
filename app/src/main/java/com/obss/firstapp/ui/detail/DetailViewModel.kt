package com.obss.firstapp.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obss.firstapp.model.actor.Actor
import com.obss.firstapp.model.credit.Cast
import com.obss.firstapp.model.movieDetail.MovieDetail
import com.obss.firstapp.model.movieDetail.MovieImage
import com.obss.firstapp.model.movieDetail.MoviePoster
import com.obss.firstapp.model.review.ReviewResult
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

    private val _movieImages = MutableStateFlow<List<MoviePoster>>(listOf())
    val movieImages: StateFlow<List<MoviePoster>> = _movieImages

    private val _movieCasts = MutableStateFlow<List<Cast>>(listOf())
    val movieCasts: StateFlow<List<Cast>> = _movieCasts

    private val _movieReviews = MutableStateFlow<List<ReviewResult>>(listOf())
    val movieReviews: StateFlow<List<ReviewResult>> = _movieReviews


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

    fun getMovieImages(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = movieApi.getMovieImages(movieId)
                _movieImages.value = response.posters!!
            } catch (exception: Exception) {
                catchException(exception)
            }
        }
    }

    fun getMovieCasts(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = movieApi.getMovieCredits(movieId)
                _movieCasts.value = response.cast
            } catch (exception: Exception) {
                catchException(exception)
            }
        }
    }

    fun getMovieReviews(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = movieApi.getMovieReviews(movieId)
                _movieReviews.value = response.results
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