package com.obss.firstapp.ui.view.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obss.firstapp.data.model.movieSearch.MovieSearch
import com.obss.firstapp.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) : ViewModel() {
        private val _searchMovieList = MutableStateFlow<List<MovieSearch>>(emptyList())
        val searchMovieList: StateFlow<List<MovieSearch>> = _searchMovieList

        private val _loadingStateFlow = MutableStateFlow(false)
        val loadingStateFlow: StateFlow<Boolean> = _loadingStateFlow

        private val _errorMessage = MutableStateFlow("")
        val errorMessage: StateFlow<String> = _errorMessage

        fun searchMovies(query: String) {
            viewModelScope.launch {
                movieRepository.searchMovies(query, _searchMovieList, _loadingStateFlow, _errorMessage)
            }
        }
    }
