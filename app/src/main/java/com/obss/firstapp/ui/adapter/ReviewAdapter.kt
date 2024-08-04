package com.obss.firstapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.obss.firstapp.databinding.ReviewItemBinding
import com.obss.firstapp.model.review.ReviewResult

class ReviewAdapter() : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    private var reviewList = listOf<ReviewResult>()

    inner class ViewHolder(val binding: ReviewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = reviewList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvAuthorUsername.text = reviewList[position].authorDetails.username
        holder.binding.tvReviewRating.text = reviewList[position].authorDetails.rating.toString()
        holder.binding.tvReviewContent.text = reviewList[position].content
        holder.binding.tvReviewDate.text = reviewList[position].createdAt
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(reviewList[position]) }
        }
    }

    private var onItemClickListener: ((ReviewResult) -> Unit)? = null

    fun setOnItemClickListener(listener: (ReviewResult) -> Unit) {
        onItemClickListener = listener
    }

    fun updateList(newList: List<ReviewResult>) {
        reviewList = newList
        notifyDataSetChanged()
    }


}