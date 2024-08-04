package com.obss.firstapp.ui.review

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obss.firstapp.model.review.ReviewResult
import com.obss.firstapp.network.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(private val movieApi: MovieApiService): ViewModel() {

    private var _reviewList = MutableStateFlow<List<ReviewResult>>(emptyList())
    val reviewList: StateFlow<List<ReviewResult>> = _reviewList

    private val _loadingStateFlow = MutableStateFlow(false)
    val loadingStateFlow: StateFlow<Boolean> = _loadingStateFlow

    fun getReviews(movieId: Int) {
        _loadingStateFlow.value = true
        try {
            viewModelScope.launch {
                val response = movieApi.getMovieReviews(movieId)
                _reviewList.value = response.results
            }
        } catch (exception: Exception) {
            catchException(exception)
        } finally {
            _loadingStateFlow.value = false
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