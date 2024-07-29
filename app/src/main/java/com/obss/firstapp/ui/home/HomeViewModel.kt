package com.obss.firstapp.ui.home

import androidx.lifecycle.ViewModel
import com.obss.firstapp.network.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val movieApi: MovieApiService): ViewModel() {

}