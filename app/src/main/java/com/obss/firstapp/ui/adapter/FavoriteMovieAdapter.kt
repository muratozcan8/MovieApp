package com.obss.firstapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.obss.firstapp.databinding.MovieGridItemBinding
import com.obss.firstapp.ext.roundToSingleDecimal
import com.obss.firstapp.room.FavoriteMovie
import com.obss.firstapp.utils.Constants.IMAGE_BASE_URL

class FavoriteMovieAdapter : RecyclerView.Adapter<FavoriteMovieAdapter.ViewHolder>() {
    private var favoriteMovieList = listOf<FavoriteMovie>()

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

    override fun getItemCount(): Int = favoriteMovieList.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val favoriteMovie = favoriteMovieList[position]
        holder.binding.tvMovieGrid.text = favoriteMovie.title
        holder.binding.ivMovieGrid.load("$IMAGE_BASE_URL${favoriteMovie.posterPath}")
        holder.binding.tvMovieScoreGrid.text = favoriteMovie.averageVote?.roundToSingleDecimal()
        holder.binding.ibMovieFavGrid.visibility = View.VISIBLE
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(favoriteMovie) }
        }
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
