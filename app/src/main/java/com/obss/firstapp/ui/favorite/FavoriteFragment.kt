package com.obss.firstapp.ui.favorite

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.obss.firstapp.databinding.FragmentFavoriteBinding
import com.obss.firstapp.ext.collectFlow
import com.obss.firstapp.ui.MainActivity
import com.obss.firstapp.ui.adapter.FavoriteMovieAdapter
import com.obss.firstapp.utils.DialogHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModels()

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
        showErrorDialog()
    }

    private fun getFavoriteMovies() {
        viewModel.getAllFavoriteMovies()
        collectFlow {
            viewModel.favoriteMovies.collect { favoriteMovieList ->
                val adapter = FavoriteMovieAdapter()
                binding.rvFavoriteMovie.adapter = adapter
                binding.rvFavoriteMovie.layoutManager = GridLayoutManager(context, SPAN_COUNT)
                adapter.updateList(favoriteMovieList)
                adapter.setOnItemClickListener {
                    val direction = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(it.movieId!!)
                    findNavController().navigate(direction)
                }
            }
        }
    }

    private fun showErrorDialog() {
        collectFlow {
            viewModel.errorMessage.collect {
                if (it.isNotEmpty()) {
                    DialogHelper.showCustomAlertDialog(requireContext(), it)
                }
            }
        }
    }

    private fun changeSpanCount() {
        val isLandscape =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        SPAN_COUNT = if (isLandscape) SPAN_COUNT_LANDSCAPE_GRID else SPAN_COUNT_GRID
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
