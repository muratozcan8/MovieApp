package com.obss.firstapp.data.network

import com.obss.firstapp.data.model.actor.Actor
import com.obss.firstapp.data.model.credit.Credits
import com.obss.firstapp.data.model.movie.MovieList
import com.obss.firstapp.data.model.movieDetail.MovieDetail
import com.obss.firstapp.data.model.movieDetail.MovieImage
import com.obss.firstapp.data.model.movieSearch.MovieSearchResult
import com.obss.firstapp.data.model.review.Review
import com.obss.firstapp.data.model.video.Video
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
    ): MovieList

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int,
    ): MovieList

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int,
    ): MovieList

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
    ): MovieDetail

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
    ): MovieSearchResult

    @GET("search/movie")
    suspend fun searchMoviesHome(
        @Query("page") page: Int,
        @Query("query") query: String,
    ): MovieList

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
    ): Credits

    @GET("person/{person_id}")
    suspend fun getActorDetails(
        @Path("person_id") personId: Int,
    ): Actor

    @GET("movie/{movie_id}/recommendations")
    suspend fun getMovieRecommendations(
        @Path("movie_id") movieId: Int,
    ): MovieList

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
