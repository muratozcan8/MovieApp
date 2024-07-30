package com.obss.firstapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.obss.firstapp.databinding.MovieGridItemBinding
import com.obss.firstapp.model.movie.Movie

class PopularMovieAdapter() : RecyclerView.Adapter<PopularMovieAdapter.ViewHolder>() {

    private var popularMovieList: List<Movie> = listOf()

    inner class ViewHolder(val binding: MovieGridItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = popularMovieList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = popularMovieList[position]
        holder.binding.ivMovieGrid.load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
        holder.binding.tvMovieGrid.text = movie.title
        holder.binding.tvMovieScoreGrid.text = movie.voteAverage.toString()
    }

    fun updateList(newList: List<Movie>) {
        popularMovieList = newList
        notifyDataSetChanged()
    }


}