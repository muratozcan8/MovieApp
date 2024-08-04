package com.obss.firstapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.obss.firstapp.databinding.ReviewItemBinding
import com.obss.firstapp.model.review.ReviewResult
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class ReviewAdapter() : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    private var reviewList = listOf<ReviewResult>()

    inner class ViewHolder(val binding: ReviewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = reviewList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.ivAuthor.load(reviewList[position].authorDetails.avatarPath)
        holder.binding.tvAuthorUsername.text = reviewList[position].authorDetails.username
        holder.binding.tvReviewRating.text = reviewList[position].authorDetails.rating.toString() + "/10"
        holder.binding.tvReviewContent.text = reviewList[position].content
        holder.binding.tvReviewDate.text = formatDate(reviewList[position].createdAt)
    }

    fun updateList(newList: List<ReviewResult>) {
        reviewList = newList
        notifyDataSetChanged()
    }

    private fun formatDate(dateString: String): String {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormatter.timeZone = TimeZone.getTimeZone("UTC")

        val birthDate = inputFormatter.parse(dateString)

        val outputFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
        val formattedDate = birthDate?.let { outputFormatter.format(it) }

        return "$formattedDate"
    }


}