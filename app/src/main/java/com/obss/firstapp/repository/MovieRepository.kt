package com.obss.firstapp.repository

import com.obss.firstapp.network.MovieApiService
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
    }
