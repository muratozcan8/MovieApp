package com.obss.firstapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.obss.firstapp.data.model.movie.Movie
import com.obss.firstapp.data.repository.MovieRepository
import com.obss.firstapp.utils.ext.toLoadResultError
import kotlinx.coroutines.flow.MutableStateFlow

class NowPlayingMoviesPagingSource(
    private val movieRepository: MovieRepository,
    private val errorMessage: MutableStateFlow<String>,
) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> =
        try {
            val currentPage = params.key ?: 1
            val response = movieRepository.getNowPlayingMovies(currentPage)
            val responseData = mutableListOf<Movie>()
            responseData.addAll(response)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1),
            )
        } catch (exception: Exception) {
            exception.toLoadResultError(errorMessage)
        }
}
