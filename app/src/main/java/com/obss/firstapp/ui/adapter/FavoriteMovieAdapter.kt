package com.obss.firstapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.obss.firstapp.databinding.MovieGridItemBinding
import com.obss.firstapp.databinding.MovieItemLinearBinding
import com.obss.firstapp.ext.roundToSingleDecimal
import com.obss.firstapp.room.FavoriteMovie
import com.obss.firstapp.ui.adapter.PopularMovieAdapter.GridViewHolder
import com.obss.firstapp.ui.adapter.PopularMovieAdapter.LinearViewHolder
import com.obss.firstapp.utils.Constants.IMAGE_BASE_URL
import javax.inject.Inject

class FavoriteMovieAdapter
    @Inject
    constructor(
        private var isGridLayout: Boolean,
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var favoriteMovieList = listOf<FavoriteMovie>()

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

        override fun getItemCount(): Int = favoriteMovieList.size

        override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int,
        ) {
            val favoriteMovie = favoriteMovieList[position]
            if (holder is GridViewHolder) {
                holder.binding.ivMovieGrid.load("$IMAGE_BASE_URL${favoriteMovie.posterPath}")
                holder.binding.tvMovieGrid.text = favoriteMovie.title
                holder.binding.tvMovieScoreGrid.text = favoriteMovie.averageVote?.roundToSingleDecimal()
                holder.binding.ibMovieFavGrid.visibility = View.VISIBLE
            } else if (holder is LinearViewHolder) {
                holder.binding.ivMovieLinear.load("${IMAGE_BASE_URL}${favoriteMovie.posterPath}")
                holder.binding.tvMovieLinear.text = favoriteMovie.title
                holder.binding.tvMovieScoreLinear.text = favoriteMovie.averageVote?.roundToSingleDecimal()
                holder.binding.ibMovieFavLinear.visibility = View.VISIBLE
            }
            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(favoriteMovie) }
            }
            holder.setIsRecyclable(false)
        }

        private var onItemClickListener: ((FavoriteMovie) -> Unit)? = null

        fun setOnItemClickListener(listener: (FavoriteMovie) -> Unit) {
            onItemClickListener = listener
        }

        fun updateList(newList: List<FavoriteMovie>) {
            favoriteMovieList = newList
            notifyDataSetChanged()
        }
    }
