package com.obss.firstapp.ui.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.obss.firstapp.databinding.FragmentReviewBinding
import com.obss.firstapp.ext.collectFlow
import com.obss.firstapp.model.review.ReviewResult
import com.obss.firstapp.ui.adapter.ReviewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private lateinit var binding: FragmentReviewBinding
    private val viewModel: ReviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = arguments?.getInt("movieId")
        viewModel.getReviews(movieId!!)
        collectFlow {
            viewModel.reviewList.collect {
                initGenresRecyclerAdapter(it)
            }
        }
        collectFlow {
            viewModel.loadingStateFlow.collect {
                binding.progressBarReview.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }


    private fun initGenresRecyclerAdapter(categoryList : List<ReviewResult>) {
        val adapter = ReviewAdapter()
        binding.rvReviews.adapter = adapter
        adapter.updateList(categoryList)
    }

}