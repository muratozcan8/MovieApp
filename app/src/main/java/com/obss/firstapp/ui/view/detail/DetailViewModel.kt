package com.obss.firstapp.ui.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obss.firstapp.data.local.FavoriteMovie
import com.obss.firstapp.data.model.actor.Actor
import com.obss.firstapp.data.model.credit.Cast
import com.obss.firstapp.data.model.movie.Movie
import com.obss.firstapp.data.model.movieDetail.MovieDetail
import com.obss.firstapp.data.model.movieDetail.MoviePoster
import com.obss.firstapp.data.model.review.ReviewResult
import com.obss.firstapp.data.model.video.VideoResult
import com.obss.firstapp.data.repository.MovieRepository
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

        private val _videos = MutableStateFlow<List<VideoResult>>(listOf())
        val videos: StateFlow<List<VideoResult>> = _videos

        private val _isLoadingsActorDetail = MutableStateFlow(false)
        val isLoadingsActorDetail: StateFlow<Boolean> = _isLoadingsActorDetail

        private val _errorMessage = MutableStateFlow("")
        val errorMessage: StateFlow<String> = _errorMessage

        private val _errorMessageActorDetail = MutableStateFlow("")
        val errorMessageActorDetail: StateFlow<String> = _errorMessageActorDetail

        fun getMovieDetails(movieId: Int) {
            viewModelScope.launch {
                movieRepository.getMovieDetails(movieId, _movie, _isLoadings, _errorMessage)
            }
        }

        fun getMovieImages(movieId: Int) {
            viewModelScope.launch {
                movieRepository.getMovieImages(movieId, _movieImages, _isLoadings, _errorMessage)
            }
        }

        fun getMovieCasts(movieId: Int) {
            viewModelScope.launch {
                movieRepository.getMovieCredits(movieId, _movieCasts, _isLoadings, _errorMessage)
            }
        }

        fun getActorDetails(actorId: Int) {
            _actor.value = null
            viewModelScope.launch {
                movieRepository.getActorDetails(actorId, _actor, _isLoadingsActorDetail, _errorMessageActorDetail)
                _errorMessageActorDetail.value = ""
            }
        }

        fun getRecommendationMovies(movieId: Int) {
            viewModelScope.launch {
                movieRepository.getMovieRecommendations(movieId, _recommendationMovies, _isLoadings, _errorMessage)
            }
        }

        fun getReviews(movieId: Int) {
            viewModelScope.launch {
                movieRepository.getMovieReviews(movieId, _reviews, _isLoadings, _errorMessage)
            }
        }

        fun addFavoriteMovie(favoriteMovie: FavoriteMovie) {
            viewModelScope.launch {
                movieRepository.insertMovie(favoriteMovie, _errorMessage)
            }
        }

        fun removeFavoriteMovie(favoriteMovie: FavoriteMovie) {
            viewModelScope.launch {
                movieRepository.deleteMovie(favoriteMovie, _errorMessage)
            }
        }

        fun getVideos(movieId: Int) {
            viewModelScope.launch {
                movieRepository.getMovieVideos(movieId, _videos, _isLoadings, _errorMessage)
            }
        }

        fun getFavoriteMovie(movieId: Int) {
            viewModelScope.launch {
                try {
                    val response = movieRepository.getMovieById(movieId)
                    _favoriteMovie.value = response
                } catch (exception: Exception) {
                    movieRepository.catchException(exception, _errorMessage)
                }
            }
        }
    }
