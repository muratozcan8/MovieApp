package com.obss.firstapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.obss.firstapp.data.model.movie.Movie
import com.obss.firstapp.data.repository.MovieRepository
import com.obss.firstapp.utils.ext.toLoadResultError
import kotlinx.coroutines.flow.MutableStateFlow

class SearchMoviePagingSource(
    private val movieRepository: MovieRepository,
    private val errorMessage: MutableStateFlow<String>,
    private val query: String,
) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> =
        try {
            val currentPage = params.key ?: 1
            val response = movieRepository.searchMoviesHome(currentPage, query)
            val responseData = mutableListOf<Movie>()
            responseData.addAll(response.results)

            val totalPages = response.totalPages

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (currentPage < totalPages) currentPage + 1 else null,
            )
        } catch (exception: Exception) {
            exception.toLoadResultError(errorMessage)
        }
}
