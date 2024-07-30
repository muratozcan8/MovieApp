package com.obss.firstapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.obss.firstapp.databinding.MovieGridItemBinding
import com.obss.firstapp.model.MovieSearch
import com.obss.firstapp.model.movie.Movie
import kotlin.math.roundToInt

class SearchMovieAdapter() : RecyclerView.Adapter<SearchMovieAdapter.ViewHolder>() {

    private var searchMovieList: List<MovieSearch> = listOf()

    inner class ViewHolder(val binding: MovieGridItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = searchMovieList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = searchMovieList[position]
        holder.binding.ivMovieGrid.load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
        holder.binding.tvMovieGrid.text = movie.title
        holder.binding.tvMovieScoreGrid.text = (((movie.voteAverage?.times(10))?.roundToInt() ?: 0) / 10.0).toString()
    }

    fun updateList(newList: List<MovieSearch>) {
        searchMovieList = newList
        notifyDataSetChanged()
    }


}