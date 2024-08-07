package com.obss.firstapp.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obss.firstapp.model.review.ReviewResult
import com.obss.firstapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) : ViewModel() {
        private var _reviewList = MutableStateFlow<List<ReviewResult>>(emptyList())
        val reviewList: StateFlow<List<ReviewResult>> = _reviewList

        private val _loadingStateFlow = MutableStateFlow(false)
        val loadingStateFlow: StateFlow<Boolean> = _loadingStateFlow

        fun getReviews(movieId: Int) {
            viewModelScope.launch {
                movieRepository.getMovieReviews(movieId, _reviewList, _loadingStateFlow)
            }
        }
    }
