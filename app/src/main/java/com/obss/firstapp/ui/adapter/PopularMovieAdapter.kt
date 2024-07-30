package com.obss.firstapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.obss.firstapp.databinding.MovieGridItemBinding
import com.obss.firstapp.databinding.MovieItemLinearBinding
import com.obss.firstapp.model.movie.Movie
import kotlin.math.roundToInt

class PopularMovieAdapter() : RecyclerView.Adapter<PopularMovieAdapter.ViewHolder>() {

    private var popularMovieList: List<Movie> = listOf()

    inner class ViewHolder(val binding: MovieItemLinearBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieItemLinearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = popularMovieList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = popularMovieList[position]
        holder.binding.ivMovieLinear.load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
        holder.binding.tvMovieLinear.text = movie.title
        holder.binding.tvMovieScoreLinear.text = (((movie.voteAverage?.times(10))?.roundToInt() ?: 0) / 10.0).toString()
    }

    fun updateList(newList: List<Movie>) {
        popularMovieList = newList
        notifyDataSetChanged()
    }


}