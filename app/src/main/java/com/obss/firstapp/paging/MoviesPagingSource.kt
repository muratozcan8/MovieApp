package com.obss.firstapp.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.network.MovieApiService
import retrofit2.HttpException

class MoviesPagingSource(private val apiService: MovieApiService) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: 1
            val response = apiService.getPopularMovies(currentPage)
            val data = response.results
            val responseData = mutableListOf<Movie>()
            responseData.addAll(data)

            Log.e("Paging", "Current Page: $currentPage")
            Log.e("Paging", "Response Data: $responseData")
            Log.e("Paging", "Data: $data")

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