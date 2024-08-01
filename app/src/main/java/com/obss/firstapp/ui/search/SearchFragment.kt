package com.obss.firstapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.obss.firstapp.databinding.FragmentSearchBinding
import com.obss.firstapp.ext.collectFlow
import com.obss.firstapp.model.movieSearch.MovieSearch
import com.obss.firstapp.ui.adapter.SearchMovieAdapter
import com.obss.firstapp.ui.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


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

        binding.etSearchMovie.addTextChangedListener { searchText ->
            collectFlow {
                val text = searchText.toString()
                delay(1000)
                if (text == searchText.toString()) viewModel.searchMovies(searchText.toString())
            }
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
        adapter.setOnItemClickListener { movie ->
            val direction = SearchFragmentDirections.actionSearchFragmentToDetailFragment(movie.id!!)
            findNavController().navigate(direction)
        }
    }

}