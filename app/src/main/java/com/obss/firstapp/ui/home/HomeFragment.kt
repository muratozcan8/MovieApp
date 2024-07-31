package com.obss.firstapp.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
        val isLandscape =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        SPAN_COUNT = if (isLandscape) SPAN_COUNT_LANDSCAPE_GRID else SPAN_COUNT_GRID

        viewModel.getPopularMovies()

        binding.ibMovieHomeLayout.setOnClickListener {
            isGridLayout = !isGridLayout
            setViewLayout(viewModel.popularMovieList.value.results)
        }

        collectFlow {
            viewModel.popularMovieList.collect {
                initRecyclerAdapter(it.results)
                setViewLayout(it.results)
            }
        }
        collectFlow {
            viewModel.loadingStateFlow.collect {
                binding.progressBarHome.visibility = if(it) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setViewLayout(popularMovieList: List<Movie>) {
        if (isGridLayout) {
            binding.ibMovieHomeLayout.setImageResource(R.drawable.linear_view_24)
            binding.rvPopularMovies.layoutManager = GridLayoutManager(context, SPAN_COUNT)
        } else {
            binding.ibMovieHomeLayout.setImageResource(R.drawable.grid_view_24)
            binding.rvPopularMovies.layoutManager = LinearLayoutManager(context)
        }
        initRecyclerAdapter(popularMovieList)
    }


    private fun initRecyclerAdapter(popularMovieList : List<Movie>) {
        val adapter = PopularMovieAdapter(isGridLayout)
        binding.rvPopularMovies.adapter = adapter
        adapter.updateList(popularMovieList)
    }

    companion object {
        var isGridLayout = true
        private var SPAN_COUNT = 3
        private var SPAN_COUNT_GRID = 3
        private var SPAN_COUNT_LANDSCAPE_GRID = 5

    }

}