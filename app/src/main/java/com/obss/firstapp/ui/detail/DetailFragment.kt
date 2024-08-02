package com.obss.firstapp.ui.detail

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.obss.firstapp.R
import com.obss.firstapp.databinding.BottomsheetDialogBinding
import com.obss.firstapp.databinding.FragmentDetailBinding
import com.obss.firstapp.ext.collectFlow
import com.obss.firstapp.model.actor.Actor
import com.obss.firstapp.model.credit.Cast
import com.obss.firstapp.model.movieDetail.Genre
import com.obss.firstapp.ui.MainActivity
import com.obss.firstapp.ui.adapter.ActorListAdapter
import com.obss.firstapp.ui.adapter.MovieCategoryAdapter
import com.obss.firstapp.ui.adapter.MovieImageAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
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
        (activity as MainActivity).changeVisibilityBottomBar(false)
        val movieId = arguments?.getInt("movieId")
        viewModel.getMovieDetails(movieId!!)
        viewModel.getMovieImages(movieId)
        viewModel.getMovieCasts(movieId)
        viewModel.getMovieReviews(movieId)

        collectFlow {
            viewModel.movie.collect { movie ->
                binding.tvMovieTitle.text = movie?.title
                binding.tvMovieScore.text = (((movie?.voteAverage?.times(10))?.roundToInt() ?: 0) / 10.0).toString()
                binding.tvMovieDate.text = movie?.releaseDate?.take(4)
                binding.tvMovieTime.text = movie?.runtime?.roundToInt().toString() + " min"
                binding.tvSummary.text = movie?.overview
                if (movie?.genres != null) initGenresRecyclerAdapter(movie.genres)
            }
        }
        collectFlow {
            viewModel.movieImages.collect { images ->
                val adapter = MovieImageAdapter()
                binding.ivMovie.adapter = adapter
                binding.ivMovie.offscreenPageLimit = 3
                binding.ivMovie.clipToPadding = false
                binding.ivMovie.setPadding(0, 0, 0, 0)
                adapter.updateList(images)
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
            findNavController().popBackStack()
        }
    }

    private fun initActorsRecyclerAdapter(actorList : List<Cast>) {
        val adapter = ActorListAdapter()
        binding.rvActors.adapter = adapter
        adapter.updateList(actorList)
        adapter.setOnItemClickListener { actor ->
            viewModel.getActorDetails(actor.id!!)
            collectFlow {
                viewModel.actor.collect { actorDetail ->
                    if (actorDetail != null && actor.id == actorDetail.id!!) {
                        showActorDialog(actorDetail)
                        cancel()
                    }

                }
            }
        }
    }

    private fun initGenresRecyclerAdapter(categoryList : List<Genre>) {
        val adapter = MovieCategoryAdapter()
        binding.rvCategory.adapter = adapter
        adapter.updateList(categoryList)
    }

    private fun showActorDialog(actor: Actor) {
        val actorDialog = layoutInflater.inflate(R.layout.bottomsheet_dialog, null)
        val dialog = BottomSheetDialog(requireContext(), R.style.DialogAnimation)
        dialog.setContentView(actorDialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<ImageView>(R.id.iv_actor_profile)?.load("https://image.tmdb.org/t/p/w500${actor.profilePath}")
        dialog.findViewById<TextView>(R.id.tv_actor_name)?.text = actor.name
        dialog.findViewById<TextView>(R.id.tv_actor_birthday)?.text = if (actor.birthday.isNullOrEmpty()) "-" else actor.birthday.toString()
        dialog.findViewById<TextView>(R.id.tv_place_of_birth)?.text = if (actor.placeOfBirth.isNullOrEmpty()) "-" else actor.placeOfBirth.toString()
        dialog.findViewById<TextView>(R.id.tv_actor_webpage)?.text = if (actor.homepage == null) "-" else actor.homepage.toString()
        dialog.findViewById<TextView>(R.id.tv_actor_biography)?.text = actor.biography
        dialog.show()
    }

}