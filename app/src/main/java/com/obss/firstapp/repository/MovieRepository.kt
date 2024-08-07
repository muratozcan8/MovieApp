package com.obss.firstapp.repository

import com.obss.firstapp.model.movieDetail.MovieDetail
import com.obss.firstapp.network.MovieApiService
import com.obss.firstapp.room.FavoriteMovie
import com.obss.firstapp.room.MovieDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository
    @Inject
    constructor(
        private val movieApiService: MovieApiService,
        private val movieDao: MovieDao,
    ) {
        suspend fun getPopularMovies(page: Int) = movieApiService.getPopularMovies(page)

        suspend fun getTopRatedMovies(page: Int) = movieApiService.getTopRatedMovies(page)

        suspend fun getNowPlayingMovies(page: Int) = movieApiService.getNowPlayingMovies(page)

        suspend fun getMovieById(movieId: Int) = movieDao.getMovieById(movieId)

        suspend fun insertMovie(favoriteMovie: FavoriteMovie) = movieDao.insertMovie(favoriteMovie)

        suspend fun deleteMovie(favoriteMovie: FavoriteMovie) = movieDao.deleteMovie(favoriteMovie)

        suspend fun getFavoriteMovies() = movieDao.getAllFavorites()

        suspend fun getMovieDetails(movieId: Int): MovieDetail {
            val response = movieApiService.getMovieDetails(movieId)
            val isFavorite = getMovieById(movieId) != null
            val updatedMovie = response.copy(isFavorite = isFavorite)
            return updatedMovie
        }

        suspend fun getMovieImages(movieId: Int) = movieApiService.getMovieImages(movieId)

        suspend fun getMovieCredits(movieId: Int) = movieApiService.getMovieCredits(movieId)

        suspend fun getActorDetails(actorId: Int) = movieApiService.getActorDetails(actorId)

        suspend fun getMovieRecommendations(movieId: Int) = movieApiService.getMovieRecommendations(movieId)

        suspend fun getMovieReviews(movieId: Int) = movieApiService.getMovieReviews(movieId)

        suspend fun searchMovies(query: String) = movieApiService.searchMovies(query)
    }
