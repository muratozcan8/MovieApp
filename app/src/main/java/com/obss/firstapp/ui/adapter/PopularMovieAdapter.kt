package com.obss.firstapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.obss.firstapp.databinding.MovieGridItemBinding
import com.obss.firstapp.databinding.MovieItemLinearBinding
import com.obss.firstapp.model.movie.Movie
import com.obss.firstapp.ui.home.HomeFragment
import kotlin.math.roundToInt

class PopularMovieAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var popularMovieList: List<Movie> = listOf()

    //inner class ViewHolder(val binding: MovieGridItemBinding) : RecyclerView.ViewHolder(binding.root) {}

    inner class GridViewHolder(val binding: MovieGridItemBinding) : RecyclerView.ViewHolder(binding.root)
    inner class LinearViewHolder(val binding: MovieItemLinearBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (HomeFragment.isGridLayout) {
            val binding = MovieGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            GridViewHolder(binding)
        } else {
            val binding = MovieItemLinearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LinearViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = popularMovieList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = popularMovieList[position]
        if (holder is GridViewHolder) {
            holder.binding.ivMovieGrid.load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
            holder.binding.tvMovieGrid.text = movie.title
            holder.binding.tvMovieScoreGrid.text = (((movie.voteAverage?.times(10))?.roundToInt() ?: 0) / 10.0).toString()
        } else if (holder is LinearViewHolder) {
            holder.binding.ivMovieLinear.load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
            holder.binding.tvMovieLinear.text = movie.title
            holder.binding.tvMovieScoreLinear.text = (((movie.voteAverage?.times(10))?.roundToInt() ?: 0) / 10.0).toString()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Movie>) {
        popularMovieList = newList
        notifyDataSetChanged()
    }


}