package com.obss.firstapp.repository

import android.util.Log
import com.obss.firstapp.model.actor.Actor
import com.obss.firstapp.model.credit.Cast
import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.model.movieDetail.MovieDetail
import com.obss.firstapp.model.movieDetail.MoviePoster
import com.obss.firstapp.model.movieSearch.MovieSearch
import com.obss.firstapp.model.review.ReviewResult
import com.obss.firstapp.network.MovieApiService
import com.obss.firstapp.room.FavoriteMovie
import com.obss.firstapp.room.MovieDao
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

        suspend fun getMovieById(movieId: Int) = movieDao.getMovieById(movieId)

        suspend fun insertMovie(favoriteMovie: FavoriteMovie) {
            try {
                movieDao.insertMovie(favoriteMovie)
            } catch (exception: Exception) {
                catchException(exception)
            }
        }

        suspend fun deleteMovie(favoriteMovie: FavoriteMovie) {
            try {
                movieDao.deleteMovie(favoriteMovie)
            } catch (exception: Exception) {
                catchException(exception)
            }
        }

        suspend fun getFavoriteMovies(favoriteMovies: MutableStateFlow<List<FavoriteMovie>>) {
            try {
                val response = movieDao.getAllFavorites()
                favoriteMovies.value = response
            } catch (exception: Exception) {
                catchException(exception)
            }
        }

        suspend fun getMovieDetails(
            movieId: Int,
            movie: MutableStateFlow<MovieDetail?>,
            isLoading: MutableStateFlow<Boolean>,
        ) {
            isLoading.value = true
            try {
                val response = movieApiService.getMovieDetails(movieId)
                val isFavorite = getMovieById(movieId) != null
                val updatedMovie = response.copy(isFavorite = isFavorite)
                movie.value = updatedMovie
            } catch (e: Exception) {
                catchException(e)
            } finally {
                isLoading.value = false
            }
        }

        suspend fun getMovieImages(
            movieId: Int,
            movieImages: MutableStateFlow<List<MoviePoster>>,
            isLoading: MutableStateFlow<Boolean>,
        ) {
            isLoading.value = true
            try {
                val response = movieApiService.getMovieImages(movieId)
                movieImages.value = response.backdrops!!
            } catch (e: Exception) {
                catchException(e)
            } finally {
                isLoading.value = false
            }
        }

        suspend fun getMovieCredits(
            movieId: Int,
            castList: MutableStateFlow<List<Cast>>,
            isLoading: MutableStateFlow<Boolean>,
        ) {
            isLoading.value = true
            try {
                val response = movieApiService.getMovieCredits(movieId)
                castList.value = response.cast
            } catch (e: Exception) {
                catchException(e)
            } finally {
                isLoading.value = false
            }
        }

        suspend fun getActorDetails(
            actorId: Int,
            actor: MutableStateFlow<Actor?>,
        ) {
            try {
                val response = movieApiService.getActorDetails(actorId)
                actor.value = response
            } catch (e: Exception) {
                catchException(e)
            }
        }

        suspend fun getMovieRecommendations(
            movieId: Int,
            recommendationsMovies: MutableStateFlow<List<Movie>>,
            isLoading: MutableStateFlow<Boolean>,
        ) {
            isLoading.value = true
            try {
                val response = movieApiService.getMovieRecommendations(movieId)
                recommendationsMovies.value = response.results
            } catch (e: Exception) {
                catchException(e)
            } finally {
                isLoading.value = false
            }
        }

        suspend fun getMovieReviews(
            movieId: Int,
            reviews: MutableStateFlow<List<ReviewResult>>,
            isLoading: MutableStateFlow<Boolean>,
        ) {
            isLoading.value = true
            try {
                val response = movieApiService.getMovieReviews(movieId)
                reviews.value = response.results
            } catch (e: Exception) {
                catchException(e)
            } finally {
                isLoading.value = false
            }
        }

        suspend fun searchMovies(
            query: String,
            searchedMovieList: MutableStateFlow<List<MovieSearch>>,
            isLoading: MutableStateFlow<Boolean>,
        ) {
            isLoading.value = true
            try {
                val response = movieApiService.searchMovies(query)
                response.results.forEach {
                    val isFavorite = getMovieById(it.id!!) != null
                    it.isFavorite = isFavorite
                }
                searchedMovieList.value = response.results
            } catch (e: Exception) {
                catchException(e)
            } finally {
                isLoading.value = false
            }
        }

        fun catchException(exception: Exception) {
            when (exception) {
                is UnknownHostException -> {
                    Log.e("network exception", "No internet connection")
                }
                is HttpException -> {
                    Log.e("network exception", "HTTP exception")
                }
                else -> Log.e("network exception", "Unknown: ${exception.message}")
            }
        }
    }
