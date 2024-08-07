package com.obss.firstapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.obss.firstapp.R
import com.obss.firstapp.databinding.ReviewItemBinding
import com.obss.firstapp.ext.formatToReadableDate
import com.obss.firstapp.model.review.ReviewResult
import com.obss.firstapp.utils.Constants.IMAGE_BASE_URL

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    private var reviewList = listOf<ReviewResult>()

    inner class ViewHolder(
        val binding: ReviewItemBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = reviewList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        if (reviewList[position].authorDetails.avatarPath !=
            null
        ) {
            holder.binding.ivAuthor.load("$IMAGE_BASE_URL${reviewList[position].authorDetails.avatarPath}")
        }
        holder.binding.tvReviewContent.maxLines = Int.MAX_VALUE
        holder.binding.tvAuthorUsername.text = reviewList[position].authorDetails.username
        holder.binding.tvReviewRating.text = (reviewList[position].authorDetails.rating * 10).toInt().toString() + "%"
        holder.binding.tvReviewContent.text = reviewList[position].content
        holder.binding.tvReviewDate.text = (reviewList[position].createdAt).formatToReadableDate()

        if (reviewList[position].content.length > MAX_LENGTH) {
            holder.binding.tvReviewSeeMore.visibility = View.VISIBLE
            if (reviewList[position].isMore) {
                updateSeeMoreText(holder, false, position)
            } else {
                updateSeeMoreText(holder, true, position)
            }
        } else {
            holder.binding.tvReviewSeeMore.visibility = View.GONE
        }

        holder.binding.tvReviewSeeMore.setOnClickListener {
            if (reviewList[position].isMore) {
                updateSeeMoreText(holder, reviewList[position].isMore, position)
                reviewList[position].isMore = false
            } else {
                updateSeeMoreText(holder, reviewList[position].isMore, position)
                reviewList[position].isMore = true
            }
        }
    }

    private fun updateSeeMoreText(
        holder: ViewHolder,
        isMore: Boolean,
        position: Int,
    ) {
        if (isMore) {
            reviewList[position].isMore = false
            holder.binding.tvReviewContent.maxLines = 10
            holder.binding.tvReviewSeeMore.text = holder.itemView.context.getString(R.string.see_more)
            val drawable = ContextCompat.getDrawable(holder.itemView.context, R.drawable.see_more_24)
            holder.binding.tvReviewSeeMore.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        } else {
            reviewList[position].isMore = true
            holder.binding.tvReviewContent.maxLines = Int.MAX_VALUE
            holder.binding.tvReviewSeeMore.text = holder.itemView.context.getString(R.string.see_less)
            val drawable = ContextCompat.getDrawable(holder.itemView.context, R.drawable.see_less_24)
            holder.binding.tvReviewSeeMore.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }
    }

    fun updateList(newList: List<ReviewResult>) {
        reviewList = newList
        notifyDataSetChanged()
    }

    companion object {
        private const val MAX_LENGTH = 450
    }
}
