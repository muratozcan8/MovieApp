package com.obss.firstapp.data.repository

import android.util.Log
import com.obss.firstapp.data.local.FavoriteMovie
import com.obss.firstapp.data.local.MovieDao
import com.obss.firstapp.data.model.actor.Actor
import com.obss.firstapp.data.model.credit.Cast
import com.obss.firstapp.data.model.movie.Movie
import com.obss.firstapp.data.model.movie.MovieList
import com.obss.firstapp.data.model.movieDetail.MovieDetail
import com.obss.firstapp.data.model.movieDetail.MoviePoster
import com.obss.firstapp.data.model.movieSearch.MovieSearch
import com.obss.firstapp.data.model.review.ReviewResult
import com.obss.firstapp.data.model.video.VideoResult
import com.obss.firstapp.data.network.MovieApiService
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository
    @Inject
    constructor(
        private val movieApiService: MovieApiService,
        private val movieDao: MovieDao,
    ) {
        suspend fun getPopularMovies(page: Int): List<Movie> {
            val response = movieApiService.getPopularMovies(page)
            val data = response.results
            data.forEach {
                val isFavorite = getMovieById(it.id!!) != null
                it.isFavorite = isFavorite
            }
            return data
        }

        suspend fun getTopRatedMovies(page: Int): List<Movie> {
            val response = movieApiService.getTopRatedMovies(page)
            val data = response.results
            data.forEach {
                val isFavorite = getMovieById(it.id!!) != null
                it.isFavorite = isFavorite
            }
            return data
        }

        suspend fun getNowPlayingMovies(page: Int): List<Movie> {
            val response = movieApiService.getNowPlayingMovies(page)
            val data = response.results
            data.forEach {
                val isFavorite = getMovieById(it.id!!) != null
                it.isFavorite = isFavorite
            }
            return data
        }

        suspend fun searchMoviesHome(
            page: Int,
            query: String,
        ): MovieList {
            val response = movieApiService.searchMoviesHome(page, query)
            val data = response.results
            data.forEach {
                val isFavorite = getMovieById(it.id!!) != null
                it.isFavorite = isFavorite
            }
            return response
        }

        suspend fun getMovieById(movieId: Int) = movieDao.getMovieById(movieId)

        suspend fun insertMovie(
            favoriteMovie: FavoriteMovie,
            errorMessage: MutableStateFlow<String>,
        ) {
            errorMessage.value = ""
            try {
                movieDao.insertMovie(favoriteMovie)
            } catch (exception: Exception) {
                catchException(exception, errorMessage)
            }
        }

        suspend fun deleteMovie(
            favoriteMovie: FavoriteMovie,
            errorMessage: MutableStateFlow<String>,
        ) {
            errorMessage.value = ""
            try {
                movieDao.deleteMovie(favoriteMovie)
            } catch (exception: Exception) {
                catchException(exception, errorMessage)
            }
        }

        suspend fun getFavoriteMovies(
            favoriteMovies: MutableStateFlow<List<FavoriteMovie>>,
            errorMessage: MutableStateFlow<String>,
        ) {
            errorMessage.value = ""
            try {
                val response = movieDao.getAllFavorites()
                favoriteMovies.value = response
            } catch (exception: Exception) {
                catchException(exception, errorMessage)
            }
        }

        suspend fun getMovieDetails(
            movieId: Int,
            movie: MutableStateFlow<MovieDetail?>,
            isLoading: MutableStateFlow<Boolean>,
            errorMessage: MutableStateFlow<String>,
        ) {
            isLoading.value = true
            errorMessage.value = ""
            try {
                val response = movieApiService.getMovieDetails(movieId)
                val isFavorite = getMovieById(movieId) != null
                val updatedMovie = response.copy(isFavorite = isFavorite)
                movie.value = updatedMovie
            } catch (exception: Exception) {
                catchException(exception, errorMessage)
            } finally {
                isLoading.value = false
            }
        }

        suspend fun getMovieImages(
            movieId: Int,
            movieImages: MutableStateFlow<List<MoviePoster>>,
            isLoading: MutableStateFlow<Boolean>,
            errorMessage: MutableStateFlow<String>,
        ) {
            isLoading.value = true
            errorMessage.value = ""
            try {
                val response = movieApiService.getMovieImages(movieId)
                movieImages.value = response.backdrops!!
            } catch (exception: Exception) {
                catchException(exception, errorMessage)
            } finally {
                isLoading.value = false
            }
        }

        suspend fun getMovieCredits(
            movieId: Int,
            castList: MutableStateFlow<List<Cast>>,
            isLoading: MutableStateFlow<Boolean>,
            errorMessage: MutableStateFlow<String>,
        ) {
            isLoading.value = true
            errorMessage.value = ""
            try {
                val response = movieApiService.getMovieCredits(movieId)
                castList.value = response.cast
            } catch (exception: Exception) {
                catchException(exception, errorMessage)
            } finally {
                isLoading.value = false
            }
        }

        suspend fun getActorDetails(
            actorId: Int,
            actor: MutableStateFlow<Actor?>,
            isLoading: MutableStateFlow<Boolean>,
            errorMessage: MutableStateFlow<String>,
        ) {
            isLoading.value = true
            errorMessage.value = ""
            try {
                val response = movieApiService.getActorDetails(actorId)
                actor.value = response
            } catch (exception: Exception) {
                catchException(exception, errorMessage)
            } finally {
                isLoading.value = false
            }
        }

        suspend fun getMovieRecommendations(
            movieId: Int,
            recommendationsMovies: MutableStateFlow<List<Movie>>,
            isLoading: MutableStateFlow<Boolean>,
            errorMessage: MutableStateFlow<String>,
        ) {
            isLoading.value = true
            errorMessage.value = ""
            try {
                val response = movieApiService.getMovieRecommendations(movieId)
                recommendationsMovies.value = response.results
            } catch (exception: Exception) {
                catchException(exception, errorMessage)
            } finally {
                isLoading.value = false
            }
        }

        suspend fun getMovieReviews(
            movieId: Int,
            reviews: MutableStateFlow<List<ReviewResult>>,
            isLoading: MutableStateFlow<Boolean>,
            errorMessage: MutableStateFlow<String>,
        ) {
            isLoading.value = true
            errorMessage.value = ""
            try {
                val response = movieApiService.getMovieReviews(movieId)
                reviews.value = response.results
            } catch (exception: Exception) {
                catchException(exception, errorMessage)
            } finally {
                isLoading.value = false
            }
        }

        suspend fun getMovieVideos(
            movieId: Int,
            videos: MutableStateFlow<List<VideoResult>>,
            isLoading: MutableStateFlow<Boolean>,
            errorMessage: MutableStateFlow<String>,
        ) {
            isLoading.value = true
            errorMessage.value = ""
            try {
                val response = movieApiService.getMovieVideos(movieId)
                videos.value = response.results
            } catch (exception: Exception) {
                catchException(exception, errorMessage)
            } finally {
                isLoading.value = false
            }
        }

        suspend fun searchMovies(
            query: String,
            searchedMovieList: MutableStateFlow<List<MovieSearch>>,
            isLoading: MutableStateFlow<Boolean>,
            errorMessage: MutableStateFlow<String>,
        ) {
            isLoading.value = true
            errorMessage.value = ""
            try {
                val response = movieApiService.searchMovies(query)
                response.results.forEach {
                    val isFavorite = getMovieById(it.id!!) != null
                    it.isFavorite = isFavorite
                }
                searchedMovieList.value = response.results
            } catch (exception: Exception) {
                catchException(exception, errorMessage)
            } finally {
                isLoading.value = false
            }
        }

        fun catchException(
            exception: Exception,
            errorMessage: MutableStateFlow<String>,
        ) {
            when (exception) {
                is UnknownHostException -> {
                    errorMessage.value = "No Internet Connection"
                    Log.e("network exception", "No internet connection")
                }
                is HttpException -> {
                    errorMessage.value = "Movie Not Found"
                    Log.e("network exception", "HTTP Exception: ${exception.message}")
                }
                else -> {
                    errorMessage.value = "An error occurred while connecting to the server"
                    Log.e("network exception", "Unknown: ${exception.message}")
                }
            }
        }
    }
