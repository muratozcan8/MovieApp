package com.obss.firstapp.ui.review

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.obss.firstapp.R
import com.obss.firstapp.databinding.FragmentReviewBinding
import com.obss.firstapp.ext.collectFlow
import com.obss.firstapp.model.review.ReviewResult
import com.obss.firstapp.ui.MainActivity
import com.obss.firstapp.ui.adapter.ReviewAdapter
import com.obss.firstapp.ui.adapter.ReviewAdapter.Companion.MAX_LENGTH
import com.obss.firstapp.utils.DialogHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment() {
    private lateinit var binding: FragmentReviewBinding
    private val viewModel: ReviewViewModel by viewModels()

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

    private fun getAllReviews() {
        setMovieName()
        getReviews()
    }

    private fun getReviews() {
        val movieId = arguments?.getInt("movieId")
        viewModel.getReviews(movieId!!)
        collectFlow {
            viewModel.reviewList.collect {
                initGenresRecyclerAdapter(it)
                binding.tvReviewScore.text =
                    if (it.isNotEmpty()) {
                        it
                            .map { it.authorDetails.rating * 10 }
                            .average()
                            .toInt()
                            .toString()
                    } else {
                        "-"
                    }
                setReviewPercentProgressBar(it)
            }
        }
    }

    private fun setReviewPercentProgressBar(reviewList: List<ReviewResult>) {
        val progress = (reviewList.map { it.authorDetails.rating * 10 }.average()).toInt()
        binding.progressBarReviewScore.progress = progress

        if (progress >= 80) {
            binding.progressBarReviewScore.setIndicatorColor(resources.getColor(R.color.green, null))
        } else if (progress >= 60) {
            binding.progressBarReviewScore.setIndicatorColor(resources.getColor(R.color.gold, null))
        } else {
            binding.progressBarReviewScore.setIndicatorColor(resources.getColor(R.color.red, null))
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

    private fun setMovieName() {
        val movieName = arguments?.getString("movieName")
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

    private fun initGenresRecyclerAdapter(categoryList: List<ReviewResult>) {
        val adapter = ReviewAdapter()
        binding.rvReviews.adapter = adapter
        adapter.updateList(categoryList)
    }

    private fun checkLandscape() {
        val isLandscape =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (isLandscape) {
            changeVisibilityBottomBar(false)
            MAX_LENGTH = 2250
        } else {
            MAX_LENGTH = 750
        }
    }

    private fun changeVisibilityBottomBar(isVisible: Boolean) {
        (activity as MainActivity).changeVisibilityBottomBar(isVisible)
    }
}
