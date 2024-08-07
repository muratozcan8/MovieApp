package com.obss.firstapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.obss.firstapp.databinding.FragmentFavoriteBinding
import com.obss.firstapp.ext.collectFlow
import com.obss.firstapp.ui.MainActivity
import com.obss.firstapp.ui.adapter.FavoriteMovieAdapter
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
        getFavoriteMovies()
    }

    private fun getFavoriteMovies() {
        viewModel.getAllFavoriteMovies()
        collectFlow {
            viewModel.favoriteMovies.collect { favoriteMovieList ->
                val adapter = FavoriteMovieAdapter()
                binding.rvFavoriteMovie.adapter = adapter
                adapter.updateList(favoriteMovieList)
                adapter.setOnItemClickListener {
                    val direction = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(it.movieId!!)
                    findNavController().navigate(direction)
                }
            }
        }
    }

    private fun changeVisibilityBottomBar(isVisible: Boolean) {
        (activity as MainActivity).changeVisibilityBottomBar(isVisible)
    }
}
