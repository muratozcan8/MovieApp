package com.obss.firstapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obss.firstapp.model.actor.Actor
import com.obss.firstapp.model.credit.Cast
import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.model.movieDetail.MovieDetail
import com.obss.firstapp.model.movieDetail.MoviePoster
import com.obss.firstapp.model.review.ReviewResult
import com.obss.firstapp.repository.MovieRepository
import com.obss.firstapp.room.FavoriteMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) : ViewModel() {
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

        private val _reviews = MutableStateFlow<List<ReviewResult>>(listOf())
        val reviews: StateFlow<List<ReviewResult>> = _reviews

        private val _favoriteMovie = MutableStateFlow<FavoriteMovie?>(null)
        val favoriteMovie: StateFlow<FavoriteMovie?> = _favoriteMovie

        private val _isLoadings = MutableStateFlow(false)
        val isLoadings: StateFlow<Boolean> = _isLoadings

        fun getMovieDetails(movieId: Int) {
            viewModelScope.launch {
                movieRepository.getMovieDetails(movieId, _movie, _isLoadings)
            }
        }

        fun getMovieImages(movieId: Int) {
            viewModelScope.launch {
                movieRepository.getMovieImages(movieId, _movieImages, _isLoadings)
            }
        }

        fun getMovieCasts(movieId: Int) {
            viewModelScope.launch {
                movieRepository.getMovieCredits(movieId, _movieCasts, _isLoadings)
            }
        }

        fun getActorDetails(actorId: Int) {
            _actor.value = null
            viewModelScope.launch {
                movieRepository.getActorDetails(actorId, _actor)
            }
        }

        fun getRecommendationMovies(movieId: Int) {
            viewModelScope.launch {
                movieRepository.getMovieRecommendations(movieId, _recommendationMovies, _isLoadings)
            }
        }

        fun getReviews(movieId: Int) {
            viewModelScope.launch {
                movieRepository.getMovieReviews(movieId, _reviews, _isLoadings)
            }
        }

        fun addFavoriteMovie(favoriteMovie: FavoriteMovie) {
            viewModelScope.launch {
                movieRepository.insertMovie(favoriteMovie)
            }
        }

        fun removeFavoriteMovie(favoriteMovie: FavoriteMovie) {
            viewModelScope.launch {
                movieRepository.deleteMovie(favoriteMovie)
            }
        }

        fun getFavoriteMovie(movieId: Int) {
            viewModelScope.launch {
                try {
                    val response = movieRepository.getMovieById(movieId)
                    _favoriteMovie.value = response
                } catch (exception: Exception) {
                    movieRepository.catchException(exception)
                }
            }
        }
    }
