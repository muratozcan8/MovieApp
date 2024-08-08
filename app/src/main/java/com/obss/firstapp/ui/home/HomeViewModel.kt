package com.obss.firstapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.paging.NowPlayingMoviesPagingSource
import com.obss.firstapp.paging.PopularMoviesPagingSource
import com.obss.firstapp.paging.TopRatedMoviesPagingSource
import com.obss.firstapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) : ViewModel() {
        private val _errorMessage = MutableStateFlow("")
        val errorMessage: StateFlow<String> = _errorMessage

        val popularMovieList: Flow<PagingData<Movie>> =
            Pager(PagingConfig(1)) {
                PopularMoviesPagingSource(movieRepository, _errorMessage)
            }.flow.cachedIn(viewModelScope)

        val topRatedMovieList: Flow<PagingData<Movie>> =
            Pager(PagingConfig(1)) {
                TopRatedMoviesPagingSource(movieRepository, _errorMessage)
            }.flow.cachedIn(viewModelScope)

        val nowPlayingMovieList: Flow<PagingData<Movie>> =
            Pager(PagingConfig(1)) {
                NowPlayingMoviesPagingSource(movieRepository, _errorMessage)
            }.flow.cachedIn(viewModelScope)
    }
