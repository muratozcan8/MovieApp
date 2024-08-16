package com.obss.firstapp.ui.review

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.obss.firstapp.R
import com.obss.firstapp.data.model.review.ReviewResult
import com.obss.firstapp.databinding.FragmentReviewBinding
import com.obss.firstapp.ui.MainActivity
import com.obss.firstapp.ui.review.ReviewAdapter.Companion.MAX_LENGTH
import com.obss.firstapp.utils.Constants.MOVIE_ID
import com.obss.firstapp.utils.Constants.MOVIE_NAME
import com.obss.firstapp.utils.DialogHelper
import com.obss.firstapp.utils.ext.collectFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment() {
    private lateinit var binding: FragmentReviewBinding
    private val viewModel: ReviewViewModel by viewModels()
    private var errorDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentReviewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        getAllReviews()
        setBackButton()
        setLoadingProgressBar()
        checkLandscape()
        showErrorDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        errorDialog?.dismiss()
        errorDialog = null
    }

    private fun getAllReviews() {
        setMovieName()
        getReviews()
    }

    private fun getReviews() {
        val movieId = arguments?.getInt(MOVIE_ID)
        viewModel.getReviews(movieId!!)
        collectFlow {
            viewModel.reviewList.collect {
                initReviewAdapter(it)
                calculateReviewScore(it)
                setReviewPercentProgressBar(it)
            }
        }
    }

    private fun calculateReviewScore(reviewList: List<ReviewResult>) {
        binding.tvReviewScore.text =
            if (reviewList.isNotEmpty()) {
                reviewList
                    .map { it.authorDetails.rating * SCORE_MULTIPLE }
                    .average()
                    .toInt()
                    .toString()
            } else {
                EMPTY
            }
    }

    private fun setReviewPercentProgressBar(reviewList: List<ReviewResult>) {
        val progress = (reviewList.map { it.authorDetails.rating * SCORE_MULTIPLE }.average()).toInt()
        binding.progressBarReviewScore.progress = progress

        if (progress >= PROGRESS_BAR_GREEN) {
            binding.progressBarReviewScore.setIndicatorColor(resources.getColor(R.color.green, null))
        } else if (progress >= PROGRESS_BAR_YELLOW) {
            binding.progressBarReviewScore.setIndicatorColor(resources.getColor(R.color.gold, null))
        } else {
            binding.progressBarReviewScore.setIndicatorColor(resources.getColor(R.color.red, null))
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

    private fun setMovieName() {
        val movieName = arguments?.getString(MOVIE_NAME)
        binding.tvReviewMovieName.text = movieName
    }

    private fun setLoadingProgressBar() {
        collectFlow {
            viewModel.loadingStateFlow.collect {
                binding.progressBarReview.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setBackButton() {
        binding.ivBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initReviewAdapter(categoryList: List<ReviewResult>) {
        val adapter = ReviewAdapter()
        binding.rvReviews.adapter = adapter
        adapter.updateList(categoryList)
    }

    private fun checkLandscape() {
        val isLandscape =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (isLandscape) {
            changeVisibilityBottomBar(false)
            MAX_LENGTH = MAX_LENGTH_LANDSCAPE
        } else {
            MAX_LENGTH = MAX_LENGTH_PORTRAIT
        }
    }

    private fun changeVisibilityBottomBar(isVisible: Boolean) {
        (activity as MainActivity).changeVisibilityBottomBar(isVisible)
    }

    companion object {
        private const val SCORE_MULTIPLE = 10
        private const val EMPTY = "-"
        private const val MAX_LENGTH_LANDSCAPE = 2250
        private const val MAX_LENGTH_PORTRAIT = 750
        private const val PROGRESS_BAR_GREEN = 80
        private const val PROGRESS_BAR_YELLOW = 60
    }
}
