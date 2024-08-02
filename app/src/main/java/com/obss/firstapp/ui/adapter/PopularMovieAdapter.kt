package com.obss.firstapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.obss.firstapp.databinding.MovieGridItemBinding
import com.obss.firstapp.databinding.MovieItemLinearBinding
import com.obss.firstapp.model.movie.Movie
import javax.inject.Inject
import kotlin.math.roundToInt

class PopularMovieAdapter @Inject constructor(
    private var isGridLayout: Boolean)
    : PagingDataAdapter<Movie, RecyclerView.ViewHolder>(differCallback) {

    inner class GridViewHolder(val binding: MovieGridItemBinding) : RecyclerView.ViewHolder(binding.root)
    inner class LinearViewHolder(val binding: MovieItemLinearBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isGridLayout) {
            val binding = MovieGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            GridViewHolder(binding)
        } else {
            val binding = MovieItemLinearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LinearViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = getItem(position)
        if (holder is GridViewHolder) {
            holder.binding.ivMovieGrid.load("https://image.tmdb.org/t/p/w500${movie?.posterPath}")
            holder.binding.tvMovieGrid.text = movie?.title
            holder.binding.tvMovieScoreGrid.text = (((movie?.voteAverage?.times(10))?.roundToInt() ?: 0) / 10.0).toString()
            if (movie != null) holder.binding.ibMovieFavGrid.visibility = if (movie.isFavorite) View.VISIBLE else View.GONE

        } else if (holder is LinearViewHolder) {
            holder.binding.ivMovieLinear.load("https://image.tmdb.org/t/p/w500${movie?.posterPath}")
            holder.binding.tvMovieLinear.text = movie?.title
            holder.binding.tvMovieScoreLinear.text = (((movie?.voteAverage?.times(10))?.roundToInt() ?: 0) / 10.0).toString()
            if (movie != null) holder.binding.ibMovieFavLinear.visibility = if (movie.isFavorite) View.VISIBLE else View.GONE
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(movie!!) }
        }
        holder.setIsRecyclable(false)
    }

    private var onItemClickListener: ((Movie) -> Unit)? = null

    fun setOnItemClickListener(listener: (Movie) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}