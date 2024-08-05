package com.obss.firstapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.network.MovieApiService
import com.obss.firstapp.room.MovieDao
import retrofit2.HttpException

class TopRatedMoviesPagingSource(private val apiService: MovieApiService, private val movieDao: MovieDao) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: 1
            val response = apiService.getTopRatedMovies(currentPage)
            val data = response.results
            val responseData = mutableListOf<Movie>()
            data.forEach {
                val isFavorite = movieDao.getMovieById(it.id!!) != null
                it.isFavorite = isFavorite
            }
            responseData.addAll(data)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (httpE: HttpException) {
            LoadResult.Error(httpE)
        }
    }
}