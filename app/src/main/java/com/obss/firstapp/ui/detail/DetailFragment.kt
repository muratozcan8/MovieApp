package com.obss.firstapp.ui.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.obss.firstapp.R
import com.obss.firstapp.databinding.FragmentDetailBinding
import com.obss.firstapp.ext.collectFlow
import com.obss.firstapp.model.actor.Actor
import com.obss.firstapp.model.credit.Cast
import com.obss.firstapp.model.movieDetail.Genre
import com.obss.firstapp.ui.MainActivity
import com.obss.firstapp.ui.adapter.ActorListAdapter
import com.obss.firstapp.ui.adapter.MovieCategoryAdapter
import com.obss.firstapp.ui.adapter.MovieImageAdapter
import com.obss.firstapp.ui.adapter.RecommendationMovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        changeVisibilityBottomBar(false)
        setLoadingProgressBar()
        getMovieDetailsInfo()
        setReviewButton()
        getRecommendationMovies()
        fillMovieDetails()
        fillMovieImages()
        fillActorDetails()
        setBackButton()
    }

    private fun initActorsRecyclerAdapter(actorList : List<Cast>) {
        val adapter = ActorListAdapter()
        binding.rvActors.adapter = adapter
        adapter.updateList(actorList)
        adapter.setOnItemClickListener { actor ->
            viewModel.getActorDetails(actor.id!!)
            binding.progressBarActorDetail.visibility = View.VISIBLE
            collectFlow {
                viewModel.actor.collect { actorDetail ->
                    if (actorDetail != null && actor.id == actorDetail.id!!) {
                        binding.progressBarActorDetail.visibility = View.GONE
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

    private fun getMovieDetailsInfo() {
        val movieId = arguments?.getInt("movieId")
        viewModel.getMovieDetails(movieId!!)
        viewModel.getMovieImages(movieId)
        viewModel.getMovieCasts(movieId)
    }

    @SuppressLint("SetTextI18n")
    private fun fillMovieDetails() {
        collectFlow {
            viewModel.movie.collect { movie ->
                binding.tvMovieTitle.text = movie?.title
                binding.tvMovieScore.text = if (movie?.voteAverage != null)
                        (((movie.voteAverage.times(10)).roundToInt()) / 10.0).toString() else "-"
                binding.tvMovieDate.text = if (movie?.releaseDate.isNullOrEmpty()) "-"
                    else movie?.releaseDate?.take(DATE_LENGTH)
                binding.tvMovieTime.text = if (movie?.runtime != null)
                    movie.runtime.roundToInt().toString() + " min" else "-"
                binding.tvSummary.text = if (movie?.overview.isNullOrEmpty()) "-" else movie?.overview
                if (movie?.genres != null) initGenresRecyclerAdapter(movie.genres)
                binding.ivFavButton.setImageResource(R.drawable.favorite_border_24)
                binding.ivFavButton.setOnClickListener {
                    Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show()
                    binding.ivFavButton.setImageResource(R.drawable.favorite_24)
                }
            }
        }
    }

    private fun fillMovieImages() {
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
    }

    private fun getRecommendationMovies() {
        val movieId = arguments?.getInt("movieId")
        viewModel.getRecommendationMovies(movieId!!)
        collectFlow {
            viewModel.recommendationMovies.collect { movies ->
                val adapter = RecommendationMovieAdapter()
                binding.rvRecommendations.adapter = adapter
                adapter.updateList(movies)
                adapter.setOnItemClickListener {
                    val direction = DetailFragmentDirections.actionDetailFragmentSelf(it.id!!)
                    findNavController().navigate(direction)
                }
            }
        }
    }

    private fun setReviewButton() {
        val movieId = arguments?.getInt("movieId")
        viewModel.getReviews(movieId!!)
        collectFlow {
            viewModel.reviews.collect {
                if (it.isNotEmpty()){
                    binding.tvReviewSee.visibility = View.VISIBLE
                    binding.tvReviewSee.text = "See Reviews (${it.size})"
                }
                binding.tvReviewSee.setOnClickListener {
                    val direction = DetailFragmentDirections.actionDetailFragmentToReviewFragment(movieId, "Bad Boys: Ride or Die")
                    findNavController().navigate(direction)
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun fillActorDetails() {
        collectFlow {
            viewModel.movieCasts.collect { casts ->
                if (casts.isNotEmpty()) {
                    initActorsRecyclerAdapter(casts.take(ACTOR_COUNT))
                    binding.tvActors.text = casts.take(ACTOR_COUNT).joinToString(", ") { it.name.toString() }
                } else {
                    binding.tvActors.text = "-"
                }
            }
        }
    }

    private fun showActorDialog(actor: Actor) {
        val actorDialog = layoutInflater.inflate(R.layout.bottomsheet_dialog, null)
        val dialog = BottomSheetDialog(requireContext(), R.style.DialogAnimation)
        dialog.setContentView(actorDialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<ImageView>(R.id.iv_actor_profile)?.load("https://image.tmdb.org/t/p/w500${actor.profilePath}")
        dialog.findViewById<TextView>(R.id.tv_actor_name)?.text = actor.name
        dialog.findViewById<TextView>(R.id.tv_actor_birthday)?.text = if (actor.birthday.isNullOrEmpty()) "-" else formatAndCalculateAge(actor.birthday)
        dialog.findViewById<TextView>(R.id.tv_place_of_birth)?.text = if (actor.placeOfBirth.isNullOrEmpty()) "-" else actor.placeOfBirth.toString()
        dialog.findViewById<TextView>(R.id.tv_actor_webpage)?.text = if (actor.homepage == null) "-" else actor.homepage.toString()
        dialog.findViewById<TextView>(R.id.tv_actor_biography)?.text = if (actor.biography.isNullOrEmpty()) "-" else actor.biography
        dialog.show()
    }

    private fun changeVisibilityBottomBar(isVisible: Boolean) {
        collectFlow {
            delay(200)
            (activity as MainActivity).changeVisibilityBottomBar(isVisible)
        }
    }

    private fun hideSystemBars() {
        val windowInsetsController = WindowCompat.getInsetsController(requireActivity().window, binding.root)
        windowInsetsController.let {
            it.hide(WindowInsetsCompat.Type.statusBars())
            it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemBars() {
        val windowInsetsController = WindowCompat.getInsetsController(requireActivity().window, binding.root)
        windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
    }

    private fun setBackButton() {
        binding.ivBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setLoadingProgressBar() {
        collectFlow {
            viewModel.isLoadings.collect {
                binding.progressBarDetail.visibility = if (it) View.VISIBLE else View.GONE
                binding.svMovieDetail.visibility = if (it) View.GONE else View.VISIBLE
            }
        }
    }

    private fun formatAndCalculateAge(dateString: String): String {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val birthDate = inputFormatter.parse(dateString)

        val outputFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
        val formattedDate = birthDate?.let { outputFormatter.format(it) }

        val birthCalendar = Calendar.getInstance()
        if (birthDate != null) {
            birthCalendar.time = birthDate
        }
        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return "$formattedDate ($age)"
    }


    companion object {
        private const val ACTOR_COUNT = 3
        private const val DATE_LENGTH = 4
    }

}