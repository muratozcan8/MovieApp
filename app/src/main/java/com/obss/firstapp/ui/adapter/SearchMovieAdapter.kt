package com.obss.firstapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.obss.firstapp.databinding.MovieGridItemBinding
import com.obss.firstapp.ext.roundToSingleDecimal
import com.obss.firstapp.model.movieSearch.MovieSearch
import com.obss.firstapp.util.Constants.IMAGE_BASE_URL

class SearchMovieAdapter : RecyclerView.Adapter<SearchMovieAdapter.ViewHolder>() {
    private var searchMovieList: List<MovieSearch> = listOf()

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

    override fun getItemCount(): Int = searchMovieList.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val movie = searchMovieList[position]
        holder.binding.ivMovieGrid.load("$IMAGE_BASE_URL${movie.posterPath}")
        holder.binding.tvMovieGrid.text = movie.title
        holder.binding.tvMovieScoreGrid.text = movie.voteAverage?.roundToSingleDecimal()
        holder.binding.ibMovieFavGrid.visibility = if (movie.isFavorite) android.view.View.VISIBLE else android.view.View.GONE
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(movie) }
        }
    }

    private var onItemClickListener: ((MovieSearch) -> Unit)? = null

    fun setOnItemClickListener(listener: (MovieSearch) -> Unit) {
        onItemClickListener = listener
    }

    fun updateList(newList: List<MovieSearch>) {
        searchMovieList = newList
        notifyDataSetChanged()
    }
}
