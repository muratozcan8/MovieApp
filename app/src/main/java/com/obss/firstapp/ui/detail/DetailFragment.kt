package com.obss.firstapp.ui.detail

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.obss.firstapp.R
import com.obss.firstapp.databinding.FragmentDetailBinding
import com.obss.firstapp.ext.collectFlow
import com.obss.firstapp.ext.roundToSingleDecimal
import com.obss.firstapp.model.credit.Cast
import com.obss.firstapp.model.movieDetail.Genre
import com.obss.firstapp.model.movieDetail.MovieDetail
import com.obss.firstapp.room.FavoriteMovie
import com.obss.firstapp.ui.MainActivity
import com.obss.firstapp.ui.adapter.ActorListAdapter
import com.obss.firstapp.ui.adapter.MovieCategoryAdapter
import com.obss.firstapp.ui.adapter.MovieImageAdapter
import com.obss.firstapp.ui.adapter.RecommendationMovieAdapter
import com.obss.firstapp.utils.Constants.YOUTUBE_APP
import com.obss.firstapp.utils.Constants.YOUTUBE_BASE_URL
import com.obss.firstapp.utils.DialogHelper
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
        showErrorDialog()
    }

    private fun initActorsRecyclerAdapter(actorList: List<Cast>) {
        val adapter = ActorListAdapter()
        binding.rvActors.adapter = adapter
        adapter.updateList(actorList)
        collectFlow {
            viewModel.isLoadingsActorDetail.collect {
                binding.progressBarActorDetail.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
        adapter.setOnItemClickListener { actor ->
            viewModel.getActorDetails(actor.id!!)
            collectFlow {
                viewModel.actor.collect { actorDetail ->
                    if (actorDetail != null && actor.id == actorDetail.id!!) {
                        showSystemBars()
                        DialogHelper.showActorDialog(
                            requireContext(),
                            actorDetail,
                            onDismissAction = { if (checkLandscapeMode()) hideSystemBars() },
                        )
                        cancel()
                    }
                }
            }
        }
    }

    private fun showSystemBars() {
        val windowInsetsController = WindowCompat.getInsetsController(requireActivity().window, binding.root)
        windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
    }

    private fun hideSystemBars() {
        val windowInsetsController = WindowCompat.getInsetsController(requireActivity().window, binding.root)
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
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
        viewModel.getVideos(movieId)
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
        setMovieVideos()
    }

    private fun setMovieVideos() {
        collectFlow {
            viewModel.videos.collect {
                if (it.isNotEmpty()) {
                    for (video in it) {
                        if (video.site == YOUTUBE && video.type == TRAILER && video.official == true) {
                            binding.ivWatchButton.isVisible = true
                            binding.ivWatchButton.setOnClickListener {
                                val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("$YOUTUBE_APP${video.key}"))
                                val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse("$YOUTUBE_BASE_URL${video.key}"))

                                try {
                                    startActivity(intentApp)
                                } catch (e: ActivityNotFoundException) {
                                    startActivity(intentBrowser)
                                }
                            }
                            break
                        }
                    }
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
        collectFlow {
            viewModel.errorMessageActorDetail.collect {
                if (it.isNotEmpty()) {
                    DialogHelper.showCustomAlertDialog(requireContext(), it)
                }
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
                    DialogHelper.showToastMessage(
                        requireContext(),
                        resources.getString(R.string.removed_movie),
                        R.drawable.custom_toast_red_background,
                        R.drawable.remove_movie_24,
                    )
                } else {
                    binding.ivFavButton.setImageResource(R.drawable.favorite_24)
                    viewModel.addFavoriteMovie(FavoriteMovie(0, movie.id, movie.title, movie.posterPath, movie.voteAverage))
                    movie.isFavorite = true
                    DialogHelper.showToastMessage(requireContext(), resources.getString(R.string.added_movie))
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

    private fun checkLandscapeMode(): Boolean {
        val isLandscape =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        return isLandscape
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

    private fun changeVisibilityBottomBar(isVisible: Boolean) {
        collectFlow {
            delay(200)
            (activity as MainActivity).changeVisibilityBottomBar(isVisible)
        }
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
        const val BIOGRAPHY_MAX_LENGTH = 750
        const val BIOGRAPHY_MAX_LINE = 20
        private const val YOUTUBE = "YouTube"
        private const val TRAILER = "Trailer"
    }
}
