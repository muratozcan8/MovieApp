package com.obss.firstapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.network.MovieApiService
import com.obss.firstapp.paging.NowPlayingMoviesPagingSource
import com.obss.firstapp.paging.PopularMoviesPagingSource
import com.obss.firstapp.paging.TopRatedMoviesPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val movieApi: MovieApiService): ViewModel() {

    val popularMovieList: Flow<PagingData<Movie>> = Pager(PagingConfig(1)) {
        PopularMoviesPagingSource(movieApi)
    }.flow.cachedIn(viewModelScope)

    val topRatedMovieList: Flow<PagingData<Movie>> = Pager(PagingConfig(1)) {
        TopRatedMoviesPagingSource(movieApi)
    }.flow.cachedIn(viewModelScope)

    val nowPlayingMovieList: Flow<PagingData<Movie>> = Pager(PagingConfig(1)) {
        NowPlayingMoviesPagingSource(movieApi)
    }.flow.cachedIn(viewModelScope)

    private fun catchException(exception: Exception) {
        when (exception) {
            is UnknownHostException -> {
                Log.e("network exception", "no network")
            }
            is HttpException -> {
                Log.e("network exception", "HTTP exception")
            }
            else -> Log.e("network exception", "unknown", exception)
        }
    }

}