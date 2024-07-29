package com.obss.firstapp.network

import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.model.movie.MovieList
import com.obss.firstapp.model.movieDetail.MovieDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int): MovieList

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): List<Movie>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
    ): MovieDetail




}