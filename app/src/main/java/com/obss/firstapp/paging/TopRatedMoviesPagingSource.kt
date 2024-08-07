package com.obss.firstapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.repository.MovieRepository
import retrofit2.HttpException

class TopRatedMoviesPagingSource(
    private val movieRepository: MovieRepository,
) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> =
        try {
            val currentPage = params.key ?: 1
            val response = movieRepository.getTopRatedMovies(currentPage)
            val responseData = mutableListOf<Movie>()
            responseData.addAll(response)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (httpE: HttpException) {
            LoadResult.Error(httpE)
        }
}
