package com.obss.firstapp.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obss.firstapp.model.actor.Actor
import com.obss.firstapp.model.credit.Cast
import com.obss.firstapp.model.movie.Movie
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

    private val _recommendationMovies = MutableStateFlow<List<Movie>>(listOf())
    val recommendationMovies: StateFlow<List<Movie>> = _recommendationMovies

    private val _actor = MutableStateFlow<Actor?>(null)
    val actor: StateFlow<Actor?> = _actor

    private val _isLoadings = MutableStateFlow(false)
    val isLoadings: StateFlow<Boolean> = _isLoadings


    fun getMovieDetails(movieId: Int) {
        _isLoadings.value = true
        viewModelScope.launch {
            try {
                val response = movieApi.getMovieDetails(movieId)
                _movie.value = response
            } catch (exception: Exception) {
                catchException(exception)
            } finally {
                _isLoadings.value = false
            }
        }
    }

    fun getMovieImages(movieId: Int) {
        _isLoadings.value = true
        viewModelScope.launch {
            try {
                val response = movieApi.getMovieImages(movieId)
                _movieImages.value = response.backdrops!!
            } catch (exception: Exception) {
                catchException(exception)
            } finally {
                _isLoadings.value = false
            }
        }
    }

    fun getMovieCasts(movieId: Int) {
        _isLoadings.value = true
        viewModelScope.launch {
            try {
                val response = movieApi.getMovieCredits(movieId)
                _movieCasts.value = response.cast
            } catch (exception: Exception) {
                catchException(exception)
            } finally {
                _isLoadings.value = false
            }
        }
    }

    fun getActorDetails(actorId: Int) {
        _actor.value = null
        viewModelScope.launch {
            try {
                val response = movieApi.getActorDetails(actorId)
                _actor.value = response
            } catch (exception: Exception) {
                catchException(exception)
            }
        }
    }

    fun getRecommendationMovies(movieId: Int) {
        _isLoadings.value = true
        viewModelScope.launch {
            try {
                val response = movieApi.getMovieRecommendations(movieId)
                _recommendationMovies.value = response.results
            } catch (exception: Exception) {
                catchException(exception)
            } finally {
                _isLoadings.value = false
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