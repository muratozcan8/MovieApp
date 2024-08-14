package com.obss.firstapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.obss.firstapp.R
import com.obss.firstapp.databinding.MovieGridItemBinding
import com.obss.firstapp.ext.roundToSingleDecimal
import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.utils.Constants.IMAGE_BASE_URL

class RecommendationMovieAdapter : RecyclerView.Adapter<RecommendationMovieAdapter.ViewHolder>() {
    private var recommendationMovieList = listOf<Movie>()

    inner class ViewHolder(
        val binding: MovieGridItemBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = MovieGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = recommendationMovieList.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val movie = recommendationMovieList[position]
        if (movie.posterPath != null) {
            holder.binding.ivMovieGrid.load("$IMAGE_BASE_URL${movie.posterPath}")
        } else {
            holder.binding.ivMovieGrid.setImageResource(
                R.drawable.image_not_supported_24,
            )
            holder.binding.ivMovieGrid.scaleType = ImageView.ScaleType.FIT_CENTER
        }
        holder.itemView.setPadding(10, 10, 10, 10)
        holder.binding.tvMovieGrid.text = movie.title
        holder.binding.tvMovieScoreGrid.text = movie.voteAverage?.roundToSingleDecimal()
        holder.binding.ibMovieFavGrid.visibility = if (movie.isFavorite) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(movie) }
        }
    }

    private var onItemClickListener: ((Movie) -> Unit)? = null

    fun setOnItemClickListener(listener: (Movie) -> Unit) {
        onItemClickListener = listener
    }

    fun updateList(newList: List<Movie>) {
        recommendationMovieList = newList
        notifyDataSetChanged()
    }
}
