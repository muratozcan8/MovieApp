package com.obss.firstapp.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obss.firstapp.model.MovieSearch
import com.obss.firstapp.network.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val movieApi: MovieApiService): ViewModel() {

    private val _searchedMovieList = MutableStateFlow<List<MovieSearch>>(emptyList())
    val searchMovieList: StateFlow<List<MovieSearch>> = _searchedMovieList

    private val _loadingStateFlow = MutableStateFlow(false)
    val loadingStateFlow: StateFlow<Boolean> = _loadingStateFlow

    fun searchMovies(query: String) {
        _loadingStateFlow.update { true }
        viewModelScope.launch {
            try {
                val response = movieApi.searchMovies(query)
                _searchedMovieList.update { response.results }
            } catch (exception: Exception) {
                catchException(exception)
            } finally {
                _loadingStateFlow.update { false }
            }

        }
    }

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