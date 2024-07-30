package com.obss.firstapp.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.obss.firstapp.R
import com.obss.firstapp.databinding.FragmentFavoriteBinding
import com.obss.firstapp.databinding.FragmentSearchBinding
import com.obss.firstapp.ext.collectFlow
import com.obss.firstapp.model.MovieSearch
import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.ui.adapter.PopularMovieAdapter
import com.obss.firstapp.ui.adapter.SearchMovieAdapter
import com.obss.firstapp.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.searchMovies("The Matrix")

        binding.etSearchMovie.setOnFocusChangeListener { view, b ->
            Log.e("Search", "Focus Changed")
        }

        collectFlow {
            viewModel.searchMovieList.collect {
                initRecyclerAdapter(it)
            }
        }

        collectFlow {
            viewModel.loadingStateFlow.collect {
                binding.progressBarSearch.visibility = if(it) View.VISIBLE else View.GONE
            }
        }
    }

    private fun initRecyclerAdapter(searchedMovieList : List<MovieSearch>) {
        val adapter = SearchMovieAdapter()
        binding.rvSearchMovie.adapter = adapter
        adapter.updateList(searchedMovieList)
    }

}