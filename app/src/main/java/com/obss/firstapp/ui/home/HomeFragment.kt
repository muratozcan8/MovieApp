package com.obss.firstapp.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.obss.firstapp.R
import com.obss.firstapp.databinding.FragmentHomeBinding
import com.obss.firstapp.ext.collectFlow
import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.ui.MainActivity
import com.obss.firstapp.ui.adapter.LoadMoreAdapter
import com.obss.firstapp.ui.adapter.PopularMovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

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
        changeVisibilityBottomBar(true)
        changeSpanCount()
        setLayoutButton()
        setMovieTypeButtons()
    }

    private fun setLayoutView(popularMovieList: PagingData<Movie>) {
        if (isGridLayout) {
            binding.ibMovieHomeLayout.setImageResource(R.drawable.linear_view_24)
            binding.rvPopularMovies.layoutManager = GridLayoutManager(context, SPAN_COUNT)
        } else {
            binding.ibMovieHomeLayout.setImageResource(R.drawable.grid_view_24)
            binding.rvPopularMovies.layoutManager = LinearLayoutManager(context)
        }
        initRecyclerAdapter(popularMovieList)
    }

    private fun checkLoadMoreMovie(adapter: PopularMovieAdapter) {
        collectFlow {
            binding.rvPopularMovies.adapter = adapter.withLoadStateFooter(
                LoadMoreAdapter{
                    adapter.retry()
                }
            )
        }
    }

    private fun getPopularMovies() {
        collectFlow {
            viewModel.popularMovieList.collect {
                initRecyclerAdapter(it)
                setLayoutView(it)
            }
        }
    }

    private fun getTopRatedMovies() {
        collectFlow {
            viewModel.topRatedMovieList.collect {
                initRecyclerAdapter(it)
                setLayoutView(it)
            }
        }
    }

    private fun setMovieTypeButtons() {
        binding.toggleButton.check(binding.mBtnPopular.id)
        getPopularMovies()
        binding.toggleButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.m_btn_popular -> {
                        getPopularMovies()
                    }
                    R.id.m_btn_top_rated -> {
                        getTopRatedMovies()
                    }
                    R.id.m_btn_now_playing -> {
                        //getNowPlayingMovies()
                    }
                }
            }
        }
    }

    private fun changeSpanCount() {
        val isLandscape =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        SPAN_COUNT = if (isLandscape) SPAN_COUNT_LANDSCAPE_GRID else SPAN_COUNT_GRID
    }

    private fun setLayoutButton() {
        binding.ibMovieHomeLayout.setOnClickListener {
            isGridLayout = !isGridLayout
            collectFlow {
                viewModel.popularMovieList.collect {
                    setLayoutView(it)
                }
            }
        }
    }

    private fun initRecyclerAdapter(popularMovieList: PagingData<Movie>) {
        val adapter = PopularMovieAdapter(isGridLayout)
        binding.rvPopularMovies.adapter = adapter
        adapter.setOnItemClickListener { movie ->
            val direction = HomeFragmentDirections.actionHomeFragmentToDetailFragment(movie.id!!)
            findNavController().navigate(direction)
        }
        collectFlow {
            adapter.submitData(popularMovieList)
        }
        collectFlow {
            adapter.loadStateFlow.collect {
                val state = it.refresh
                binding.progressBarHome.isVisible = state is LoadState.Loading
            }
        }
        checkLoadMoreMovie(adapter)
    }

    private fun changeVisibilityBottomBar(isVisible: Boolean) {
        (activity as MainActivity).changeVisibilityBottomBar(isVisible)
    }

    companion object {
        var isGridLayout = true
        private var SPAN_COUNT = 3
        private var SPAN_COUNT_GRID = 3
        private var SPAN_COUNT_LANDSCAPE_GRID = 6

    }

}