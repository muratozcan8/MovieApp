package com.obss.firstapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.obss.firstapp.R
import com.obss.firstapp.databinding.FragmentHomeBinding
import com.obss.firstapp.ext.collectFlow
import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.model.movie.MovieList
import com.obss.firstapp.ui.adapter.PopularMovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPopularMovies()
        collectFlow {
            viewModel.popularMovieList.collect {
                initRecyclerAdapter(it.results)
            }
        }
        collectFlow {
            viewModel.loadingStateFlow.collect {
                binding.progressBarHome.visibility = if(it) View.VISIBLE else View.GONE
            }
        }


    }


    private fun initRecyclerAdapter(popularMovieList : List<Movie>) {
        val adapter = PopularMovieAdapter()
        binding.rvPopularMovies.adapter = adapter
        adapter.updateList(popularMovieList)
    }

}