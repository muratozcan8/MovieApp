package com.obss.firstapp.ui.favorite

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.obss.firstapp.R
import com.obss.firstapp.databinding.FragmentFavoriteBinding
import com.obss.firstapp.ui.MainActivity
import com.obss.firstapp.utils.Constants.MARGIN_COUNT
import com.obss.firstapp.utils.DialogHelper
import com.obss.firstapp.utils.ScreenSetting
import com.obss.firstapp.utils.ext.collectFlow
import com.obss.firstapp.utils.ext.pxToDp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModels()
    private var errorDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        changeVisibilityBottomBar(true)
        changeSpanCount()
        getFavoriteMovies()
        setLayoutView()
        showErrorDialog()
        setLayoutButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        errorDialog?.dismiss()
        errorDialog = null
    }

    private fun getFavoriteMovies() {
        viewModel.getAllFavoriteMovies()
        collectFlow {
            viewModel.favoriteMovies.collect { favoriteMovieList ->
                if (favoriteMovieList.isEmpty()) {
                    binding.llTempFavoriteMovie.visibility = View.VISIBLE
                } else {
                    binding.llTempFavoriteMovie.visibility = View.GONE
                }
                val adapter = FavoriteMovieAdapter(isGridLayoutFavorite)
                binding.rvFavoriteMovie.adapter = adapter
                adapter.updateList(favoriteMovieList)
                setFavoriteMovieClickListener(adapter)
            }
        }
    }

    private fun setFavoriteMovieClickListener(adapter: FavoriteMovieAdapter) {
        adapter.setOnItemClickListener {
            val direction = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(it.movieId!!)
            findNavController().navigate(direction)
        }
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

    private fun setLayoutButton() {
        binding.ibMovieFavoriteLayout.setOnClickListener {
            isGridLayoutFavorite = !isGridLayoutFavorite
            collectFlow {
                viewModel.favoriteMovies.collect {
                    setLayoutView()
                    getFavoriteMovies()
                }
            }
        }
    }

    private fun setLayoutView() {
        if (isGridLayoutFavorite) {
            binding.ibMovieFavoriteLayout.setImageResource(R.drawable.linear_view_24)
            binding.rvFavoriteMovie.layoutManager = GridLayoutManager(context, SPAN_COUNT_FAVORITE)
        } else {
            binding.ibMovieFavoriteLayout.setImageResource(R.drawable.grid_view_24)
            binding.rvFavoriteMovie.layoutManager = LinearLayoutManager(context)
        }
        getFavoriteMovies()
    }

    private fun changeSpanCount() {
        val spanCount = getMovieItemWidth()
        SPAN_COUNT_GRID_FAVORITE = getScreenWidthDp() / spanCount
        SPAN_COUNT_LANDSCAPE_GRID_FAVORITE = getScreenWidthDp() / spanCount
        val isLandscape =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        SPAN_COUNT_FAVORITE = if (isLandscape) SPAN_COUNT_LANDSCAPE_GRID_FAVORITE else SPAN_COUNT_GRID_FAVORITE
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
                        ?.pxToDp(requireContext())!! * MARGIN_COUNT
            ).toInt()
        return screenWidthDp
    }

    private fun changeVisibilityBottomBar(isVisible: Boolean) {
        (activity as MainActivity).changeVisibilityBottomBar(isVisible)
    }

    companion object {
        var isGridLayoutFavorite = true
        private var SPAN_COUNT_FAVORITE = 3
        private var SPAN_COUNT_GRID_FAVORITE = 3
        private var SPAN_COUNT_LANDSCAPE_GRID_FAVORITE = 6
    }
}
