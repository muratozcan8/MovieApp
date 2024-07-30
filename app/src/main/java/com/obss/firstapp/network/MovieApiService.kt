package com.obss.firstapp.network

import com.obss.firstapp.model.MovieSearchResult
import com.obss.firstapp.model.actor.Actor
import com.obss.firstapp.model.actor.ActorMovies
import com.obss.firstapp.model.credit.Credits
import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.model.movie.MovieList
import com.obss.firstapp.model.movieDetail.MovieDetail
import com.obss.firstapp.model.movieDetail.MovieImage
import com.obss.firstapp.model.review.Review
import com.obss.firstapp.model.video.Video
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int
    ): MovieList

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int
    ): MovieList

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): MovieDetail

    @GET("search/movie")
    fun searchMovies(
        @Query("query") query: String,
    ): MovieSearchResult


    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int
    ): Credits

    @GET("person/{person_id}")
    suspend fun getActorDetails(
        @Path("person_id") personId: Int,
    ): Actor

    @GET("person/{person_id}/movie_credits")
    suspend fun getActorMovies(
        @Path("person_id") personId: Int,
    ): ActorMovies

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
    ): Review

    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Int,
    ): MovieImage

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
    ): Video



}