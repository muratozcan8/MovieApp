package com.obss.firstapp.ui.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.obss.firstapp.R
import com.obss.firstapp.databinding.FragmentDetailBinding
import com.obss.firstapp.ext.collectFlow
import com.obss.firstapp.ext.formatAndCalculateAge
import com.obss.firstapp.ext.roundToSingleDecimal
import com.obss.firstapp.model.actor.Actor
import com.obss.firstapp.model.credit.Cast
import com.obss.firstapp.model.movieDetail.Genre
import com.obss.firstapp.model.movieDetail.MovieDetail
import com.obss.firstapp.room.FavoriteMovie
import com.obss.firstapp.ui.MainActivity
import com.obss.firstapp.ui.adapter.ActorListAdapter
import com.obss.firstapp.ui.adapter.MovieCategoryAdapter
import com.obss.firstapp.ui.adapter.MovieImageAdapter
import com.obss.firstapp.ui.adapter.RecommendationMovieAdapter
import com.obss.firstapp.util.Constants.IMAGE_BASE_URL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private var movieName = ""
    private var isAddFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
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

    private fun initActorsRecyclerAdapter(actorList: List<Cast>) {
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

    private fun initGenresRecyclerAdapter(categoryList: List<Genre>) {
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
                movie?.title?.let { setMovieName(it) }
                binding.tvMovieTitle.text = movie?.title
                binding.tvMovieScore.text =
                    if (movie?.voteAverage != null) {
                        movie.voteAverage.roundToSingleDecimal()
                    } else {
                        "-"
                    }
                binding.tvMovieDate.text =
                    if (movie?.releaseDate.isNullOrEmpty()) {
                        "-"
                    } else {
                        movie?.releaseDate?.take(DATE_LENGTH)
                    }
                binding.tvMovieTime.text =
                    if (movie?.runtime != null) {
                        movie.runtime.roundToInt().toString() + " min"
                    } else {
                        "-"
                    }
                binding.tvSummary.text = if (movie?.overview.isNullOrEmpty()) "-" else movie?.overview
                if (movie?.genres != null) initGenresRecyclerAdapter(movie.genres)
                if (movie != null) {
                    if (movie.isFavorite) {
                        binding.ivFavButton.setImageResource(R.drawable.favorite_24)
                    } else {
                        binding.ivFavButton.setImageResource(R.drawable.favorite_border_24)
                    }
                }
                setFavoriteButton(movie)
            }
        }
    }

    private fun setFavoriteButton(movie: MovieDetail?) {
        binding.ivFavButton.setOnClickListener {
            if (movie != null) {
                isAddFavorite = true
                if (movie.isFavorite) {
                    binding.ivFavButton.setImageResource(R.drawable.favorite_border_24)
                    viewModel.getFavoriteMovie(movie.id!!)
                    collectFlow {
                        viewModel.favoriteMovie.collect {
                            if (it != null) {
                                viewModel.removeFavoriteMovie(it)
                                movie.isFavorite = false
                            }
                        }
                    }
                    Toast.makeText(requireContext(), resources.getString(R.string.removed_movie), Toast.LENGTH_SHORT).show()
                } else {
                    binding.ivFavButton.setImageResource(R.drawable.favorite_24)
                    viewModel.addFavoriteMovie(FavoriteMovie(0, movie.id, movie.title, movie.posterPath, movie.voteAverage))
                    movie.isFavorite = true
                    Toast.makeText(requireContext(), resources.getString(R.string.added_movie), Toast.LENGTH_SHORT).show()
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
                if (movies.isEmpty()) {
                    binding.tvRecommendations.visibility = View.GONE
                    binding.rvRecommendations.visibility = View.GONE
                } else {
                    binding.tvRecommendations.visibility = View.VISIBLE
                    binding.rvRecommendations.visibility = View.VISIBLE
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
    }

    private fun setReviewButton() {
        val movieId = arguments?.getInt("movieId")
        viewModel.getReviews(movieId!!)
        collectFlow {
            viewModel.reviews.collect {
                if (it.isNotEmpty()) {
                    binding.tvReviewSee.visibility = View.VISIBLE
                    binding.tvReviewSee.text = "${resources.getString(R.string.see_reviews)} (${it.size})"
                }
                binding.tvReviewSee.setOnClickListener {
                    val direction = DetailFragmentDirections.actionDetailFragmentToReviewFragment(movieId, getMovieName())
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
        val tvActorBiography = dialog.findViewById<TextView>(R.id.tv_actor_biography)
        val tvBiographySeeMore = dialog.findViewById<TextView>(R.id.tv_actor_biography_see_more)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<ImageView>(R.id.iv_actor_profile)?.load("$IMAGE_BASE_URL${actor.profilePath}")
        dialog.findViewById<TextView>(R.id.tv_actor_name)?.text = actor.name
        dialog.findViewById<TextView>(R.id.tv_actor_birthday)?.text =
            if (actor.birthday.isNullOrEmpty()) "-" else actor.birthday.formatAndCalculateAge()
        dialog.findViewById<TextView>(R.id.tv_place_of_birth)?.text =
            if (actor.placeOfBirth.isNullOrEmpty()) "-" else actor.placeOfBirth.toString()
        dialog.findViewById<TextView>(R.id.tv_actor_webpage)?.text = if (actor.homepage == null) "-" else actor.homepage.toString()
        if (actor.biography.isNullOrEmpty()) {
            tvActorBiography?.text = "-"
        } else {
            tvActorBiography?.text = actor.biography
            setActorDetailsSeeMoreButton(actor, tvActorBiography, tvBiographySeeMore)
        }
        dialog.show()
    }

    private fun setActorDetailsSeeMoreButton(
        actor: Actor,
        tvActorBiography: TextView?,
        tvBiographySeeMore: TextView?,
    ) {
        if (actor.biography?.length!! > BIOGRAPHY_MAX_LENGTH) {
            tvBiographySeeMore?.visibility = View.VISIBLE
            tvActorBiography?.maxLines = BIOGRAPHY_MAX_LINE
            tvBiographySeeMore?.setOnClickListener {
                if (tvActorBiography?.maxLines == BIOGRAPHY_MAX_LINE) {
                    tvActorBiography.maxLines = Int.MAX_VALUE
                    tvBiographySeeMore.text = resources.getString(R.string.see_less)
                    val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.see_less_24)
                    tvBiographySeeMore.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                } else {
                    tvActorBiography?.maxLines = BIOGRAPHY_MAX_LINE
                    tvBiographySeeMore.text = resources.getString(R.string.see_more)
                    val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.see_more_24)
                    tvBiographySeeMore.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                }
            }
        }
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
            parentFragmentManager.setFragmentResult("popBackStackResult", bundleOf("isPopBackStack" to isAddFavorite))
            findNavController().popBackStack()
        }
    }

    private fun getMovieName(): String = movieName

    private fun setMovieName(newMovieName: String) {
        movieName = newMovieName
    }

    private fun setLoadingProgressBar() {
        collectFlow {
            viewModel.isLoadings.collect {
                binding.progressBarDetail.visibility = if (it) View.VISIBLE else View.GONE
                binding.svMovieDetail.visibility = if (it) View.GONE else View.VISIBLE
            }
        }
    }

    companion object {
        private const val ACTOR_COUNT = 3
        private const val DATE_LENGTH = 4
        private const val BIOGRAPHY_MAX_LENGTH = 400
        private const val BIOGRAPHY_MAX_LINE = 15
    }
}
