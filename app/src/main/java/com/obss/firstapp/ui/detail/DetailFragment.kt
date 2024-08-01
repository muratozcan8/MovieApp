package com.obss.firstapp.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.obss.firstapp.databinding.FragmentDetailBinding
import com.obss.firstapp.ext.collectFlow
import com.obss.firstapp.model.credit.Cast
import com.obss.firstapp.model.movieDetail.Genre
import com.obss.firstapp.ui.adapter.ActorListAdapter
import com.obss.firstapp.ui.adapter.MovieCategoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = arguments?.getInt("movieId")
        viewModel.getMovieDetails(movieId!!)
        viewModel.getMovieImages(movieId)
        viewModel.getMovieCasts(movieId)
        viewModel.getMovieReviews(movieId)

        collectFlow {
            viewModel.movie.collect { movie ->
                binding.tvMovieTitle.text = movie?.title
                binding.tvMovieScore.text = (((movie?.voteAverage?.times(10))?.roundToInt() ?: 0) / 10.0).toString()
                binding.tvMovieTime.text = movie?.runtime?.roundToInt().toString() + " min"
                binding.tvSummary.text = movie?.overview
                if (movie?.genres != null) initGenresRecyclerAdapter(movie?.genres!!)
            }
        }
        collectFlow {
            viewModel.movieImages.collect { images ->
                Log.e("images", images.toString())
                if (images.isNotEmpty()) binding.ivMovie.load("https://image.tmdb.org/t/p/w500${images[1].filePath}")
            }
        }

        collectFlow {
            viewModel.movieCasts.collect { casts ->
                if (casts.isNotEmpty()) {
                    initActorsRecyclerAdapter(casts.take(3))
                    binding.tvActors.text = casts.take(3).joinToString(", ") { it.name.toString() }
                }
            }
        }

        collectFlow {
            viewModel.movieReviews.collect { reviews ->
                //Log.e("reviews", reviews.toString())
            }
        }

        binding.ivBackButton.setOnClickListener {
            val action = DetailFragmentDirections.actionDetailFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun initActorsRecyclerAdapter(actorList : List<Cast>) {
        val adapter = ActorListAdapter()
        binding.rvActors.adapter = adapter
        adapter.updateList(actorList)
    }

    private fun initGenresRecyclerAdapter(categoryList : List<Genre>) {
        val adapter = MovieCategoryAdapter()
        binding.rvCategory.adapter = adapter
        adapter.updateList(categoryList)
    }

}