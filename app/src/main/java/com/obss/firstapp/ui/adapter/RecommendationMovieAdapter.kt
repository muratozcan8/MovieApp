package com.obss.firstapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.obss.firstapp.databinding.MovieGridItemBinding
import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.util.Constants.IMAGE_BASE_URL
import kotlin.math.roundToInt

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
        holder.binding.ivMovieGrid.load("$IMAGE_BASE_URL${movie.posterPath}")
        holder.binding.tvMovieGrid.text = movie.title
        holder.binding.tvMovieScoreGrid.text = (((movie.voteAverage?.times(10))?.roundToInt() ?: 0) / 10.0).toString()
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
