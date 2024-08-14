package com.obss.firstapp.ui.view.search

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.obss.firstapp.R
import com.obss.firstapp.data.model.movieSearch.MovieSearch
import com.obss.firstapp.databinding.FragmentSearchBinding
import com.obss.firstapp.ui.adapter.SearchMovieAdapter
import com.obss.firstapp.ui.view.MainActivity
import com.obss.firstapp.utils.DialogHelper
import com.obss.firstapp.utils.ScreenSetting
import com.obss.firstapp.utils.ext.collectFlow
import com.obss.firstapp.utils.ext.pxToDp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private var errorDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        changeVisibilityBottomBar(true)
        changeSpanCount()
        searchMovie()
        setSearchMovieListAdapter()
        setLoadingProgressBar()
        showErrorDialog()
        setCancelButtonVisibility()
    }

    override fun onDestroy() {
        super.onDestroy()
        errorDialog?.dismiss()
        errorDialog = null
    }

    private fun showErrorDialog() {
        collectFlow {
            viewModel.errorMessage.collect {
                if (it.isNotEmpty()) {
                    errorDialog = DialogHelper.showCustomAlertDialog(requireContext(), it)
                }
            }
        }
    }

    private fun searchMovie() {
        binding.etSearchMovie.addTextChangedListener { searchText ->
            collectFlow {
                val text = searchText.toString()
                delay(DELAY_TIME)
                if (text == searchText.toString()) viewModel.searchMovies(searchText.toString())
            }
        }
    }

    private fun setSearchMovieListAdapter() {
        collectFlow {
            viewModel.searchMovieList.collect {
                if (it.isEmpty() && binding.etSearchMovie.text.isNotEmpty()) {
                    binding.llNotFoundSearchedMovie.visibility = View.VISIBLE
                } else {
                    binding.llNotFoundSearchedMovie.visibility = View.GONE
                }
                initRecyclerAdapter(it)
            }
        }
    }

    private fun setCancelButtonVisibility() {
        binding.etSearchMovie.addTextChangedListener {
            if (binding.etSearchMovie.text.isNotEmpty()) {
                binding.ivSearchMovieCancel.visibility = View.VISIBLE
            } else {
                binding.ivSearchMovieCancel.visibility = View.GONE
                binding.llNotFoundSearchedMovie.visibility = View.GONE
            }
        }
        setCancelButtonListener()
    }

    private fun setCancelButtonListener() {
        binding.ivSearchMovieCancel.setOnClickListener {
            binding.etSearchMovie.text.clear()
        }
    }

    private fun setLoadingProgressBar() {
        collectFlow {
            viewModel.loadingStateFlow.collect {
                binding.progressBarSearch.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }

    private fun changeVisibilityBottomBar(isVisible: Boolean) {
        (activity as MainActivity).changeVisibilityBottomBar(isVisible)
    }

    private fun changeSpanCount() {
        val spanCount = getMovieItemWidth()
        SPAN_COUNT_GRID_SEARCH = getScreenWidthDp() / spanCount
        SPAN_COUNT_LANDSCAPE_GRID_SEARCH = getScreenWidthDp() / spanCount
        val isLandscape =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        SPAN_COUNT_SEARCH = if (isLandscape) SPAN_COUNT_LANDSCAPE_GRID_SEARCH else SPAN_COUNT_GRID_SEARCH
    }

    private fun getMovieItemWidth(): Int {
        val movieItemWidth =
            (
                context
                    ?.resources
                    ?.getDimension(R.dimen.movie_grid_item_width)
                    ?.toInt()
                    ?.pxToDp(requireContext())!!
            ).toInt()

        return movieItemWidth
    }

    private fun getScreenWidthDp(): Int {
        val screenWidth = ScreenSetting.getScreenWidth(requireContext())
        val screenWidthDp =
            (
                screenWidth.pxToDp(requireContext()).toInt() -
                    context
                        ?.resources
                        ?.getDimension(R.dimen.rv_margin_horizontal)
                        ?.toInt()
                        ?.pxToDp(requireContext())!! * 2
            ).toInt()
        return screenWidthDp
    }

    private fun initRecyclerAdapter(searchedMovieList: List<MovieSearch>) {
        val adapter = SearchMovieAdapter()
        binding.rvSearchMovie.adapter = adapter
        binding.rvSearchMovie.layoutManager = GridLayoutManager(context, SPAN_COUNT_SEARCH)
        adapter.updateList(searchedMovieList)
        setMovieClickListener(adapter)
    }

    private fun setMovieClickListener(adapter: SearchMovieAdapter) {
        adapter.setOnItemClickListener { movie ->
            val direction = SearchFragmentDirections.actionSearchFragmentToDetailFragment(movie.id!!)
            findNavController().navigate(direction)
        }
    }

    companion object {
        private var SPAN_COUNT_SEARCH = 3
        private var SPAN_COUNT_GRID_SEARCH = 3
        private var SPAN_COUNT_LANDSCAPE_GRID_SEARCH = 6
        private const val DELAY_TIME = 800L
    }
}
