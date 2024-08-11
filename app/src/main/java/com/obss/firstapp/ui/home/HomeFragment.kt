package com.obss.firstapp.ui.home

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.obss.firstapp.R
import com.obss.firstapp.databinding.FragmentHomeBinding
import com.obss.firstapp.ext.collectFlow
import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.ui.MainActivity
import com.obss.firstapp.ui.adapter.LoadMoreAdapter
import com.obss.firstapp.ui.adapter.PopularMovieAdapter
import com.obss.firstapp.utils.DialogHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {}
            },
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        changeVisibilityBottomBar(true)
        changeSpanCount()
        setLayoutButton()
        setMovieTypeButtons()
        checkPopBackStack()
        showErrorDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog?.dismiss()
        dialog = null
    }

    private fun showErrorDialog() {
        collectFlow {
            viewModel.errorMessage.collect {
                if (it.isNotEmpty()) {
                    dialog = DialogHelper.showCustomAlertDialog(requireContext(), it)
                }
            }
        }
    }

    private fun checkPopBackStack() {
        setFragmentResultListener("popBackStackResult") { _, bundle ->
            val result = bundle.getBoolean("isPopBackStack")
            if (result) {
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }

    private fun setLayoutView(movieList: PagingData<Movie>) {
        if (isGridLayout) {
            binding.ibMovieHomeLayout.setImageResource(R.drawable.linear_view_24)
            binding.rvPopularMovies.layoutManager = GridLayoutManager(context, SPAN_COUNT)
        } else {
            binding.ibMovieHomeLayout.setImageResource(R.drawable.grid_view_24)
            binding.rvPopularMovies.layoutManager = LinearLayoutManager(context)
        }
        initRecyclerAdapter(movieList)
    }

    private fun checkLoadMoreMovie(adapter: PopularMovieAdapter) {
        collectFlow {
            binding.rvPopularMovies.adapter =
                adapter.withLoadStateFooter(
                    LoadMoreAdapter {
                        adapter.retry()
                    },
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

    private fun getNowPlayingMovies() {
        collectFlow {
            viewModel.nowPlayingMovieList.collect {
                initRecyclerAdapter(it)
                setLayoutView(it)
            }
        }
    }

    private fun setMovieTypeButtons() {
        binding.toggleButton.check(binding.mBtnPopular.id)
        binding.mBtnPopular.isClickable = false
        binding.mBtnPopular.setTextColor(resources.getColor(R.color.black, null))
        getPopularMovies()
        MOVIE_TYPE = POPULAR
        binding.toggleButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
            setMovieTypeButton(checkedId, isChecked)
        }
    }

    private fun setMovieTypeButton(
        checkedId: Int,
        isChecked: Boolean,
    ) {
        if (isChecked) {
            when (checkedId) {
                R.id.m_btn_popular -> {
                    getPopularMovies()
                    MOVIE_TYPE = POPULAR
                    setMovieTypeButtonsVisibility(MOVIE_TYPE)
                }
                R.id.m_btn_top_rated -> {
                    getTopRatedMovies()
                    MOVIE_TYPE = TOP_RATED
                    setMovieTypeButtonsVisibility(MOVIE_TYPE)
                }
                R.id.m_btn_now_playing -> {
                    getNowPlayingMovies()
                    MOVIE_TYPE = NOW_PLAYING
                    setMovieTypeButtonsVisibility(MOVIE_TYPE)
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
                    getMoviesWithMovieType()
                }
            }
        }
    }

    private fun getMoviesWithMovieType() {
        collectFlow {
            when (MOVIE_TYPE) {
                POPULAR ->
                    viewModel.popularMovieList.collect {
                        setLayoutView(it)
                    }
                TOP_RATED ->
                    viewModel.topRatedMovieList.collect {
                        setLayoutView(it)
                    }
                NOW_PLAYING ->
                    viewModel.nowPlayingMovieList.collect {
                        setLayoutView(it)
                    }
            }
        }
    }

    private fun setMovieTypeButtonsVisibility(movieType: String) {
        binding.mBtnPopular.isClickable = movieType != POPULAR
        binding.mBtnTopRated.isClickable = movieType != TOP_RATED
        binding.mBtnNowPlaying.isClickable = movieType != NOW_PLAYING
        binding.mBtnPopular.setTextColor(
            if (movieType ==
                POPULAR
            ) {
                resources.getColor(R.color.black, null)
            } else {
                resources.getColor(R.color.white, null)
            },
        )
        binding.mBtnTopRated.setTextColor(
            if (movieType ==
                TOP_RATED
            ) {
                resources.getColor(R.color.black, null)
            } else {
                resources.getColor(R.color.white, null)
            },
        )
        binding.mBtnNowPlaying.setTextColor(
            if (movieType ==
                NOW_PLAYING
            ) {
                resources.getColor(R.color.black, null)
            } else {
                resources.getColor(R.color.white, null)
            },
        )
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
        private var MOVIE_TYPE = "movie_type"
        private var POPULAR = "popular"
        private var TOP_RATED = "top_rated"
        private var NOW_PLAYING = "now_playing"
    }
}
