package com.obss.firstapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.obss.firstapp.R
import com.obss.firstapp.databinding.MovieGridItemBinding
import com.obss.firstapp.databinding.MovieItemLinearBinding
import com.obss.firstapp.ext.roundToSingleDecimal
import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.utils.Constants.IMAGE_BASE_URL
import javax.inject.Inject

class MovieAdapter
    @Inject
    constructor(
        private var isGridLayout: Boolean,
    ) : PagingDataAdapter<Movie, RecyclerView.ViewHolder>(differCallback) {
        inner class GridViewHolder(
            val binding: MovieGridItemBinding,
        ) : RecyclerView.ViewHolder(binding.root)

        inner class LinearViewHolder(
            val binding: MovieItemLinearBinding,
        ) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): RecyclerView.ViewHolder =
            if (isGridLayout) {
                val binding = MovieGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GridViewHolder(binding)
            } else {
                val binding = MovieItemLinearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LinearViewHolder(binding)
            }

        override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int,
        ) {
            val movie = getItem(position)
            if (holder is GridViewHolder) {
                if (movie?.posterPath != null) {
                    holder.binding.ivMovieGrid.load("$IMAGE_BASE_URL${movie.posterPath}")
                } else {
                    holder.binding.ivMovieGrid.setImageResource(
                        R.drawable.image_not_supported_24,
                    )
                    holder.binding.ivMovieGrid.scaleType = ImageView.ScaleType.FIT_CENTER
                }
                holder.binding.tvMovieGrid.text = movie?.title
                holder.binding.tvMovieScoreGrid.text = movie?.voteAverage?.roundToSingleDecimal()
                if (movie != null) holder.binding.ibMovieFavGrid.visibility = if (movie.isFavorite) View.VISIBLE else View.GONE
            } else if (holder is LinearViewHolder) {
                if (movie?.posterPath != null) {
                    holder.binding.ivMovieLinear.load("$IMAGE_BASE_URL${movie.posterPath}")
                } else {
                    holder.binding.ivMovieLinear.setImageResource(
                        R.drawable.image_not_supported_24,
                    )
                    holder.binding.ivMovieLinear.scaleType = ImageView.ScaleType.FIT_CENTER
                }
                holder.binding.tvMovieLinear.text = movie?.title
                holder.binding.tvMovieScoreLinear.text = movie?.voteAverage?.roundToSingleDecimal()
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
            val differCallback =
                object : DiffUtil.ItemCallback<Movie>() {
                    override fun areItemsTheSame(
                        oldItem: Movie,
                        newItem: Movie,
                    ): Boolean = oldItem.id == newItem.id

                    @SuppressLint("DiffUtilEquals")
                    override fun areContentsTheSame(
                        oldItem: Movie,
                        newItem: Movie,
                    ): Boolean = oldItem == newItem
                }
        }
    }
