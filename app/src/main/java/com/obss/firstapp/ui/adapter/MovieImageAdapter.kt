package com.obss.firstapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.obss.firstapp.R
import com.obss.firstapp.model.movieDetail.MoviePoster

class MovieImageAdapter() : RecyclerView.Adapter<MovieImageAdapter.ViewHolder>() {

    private var movieImageList: List<MoviePoster> = listOf()

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slide_image_container, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = movieImageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.findViewById<ImageView>(R.id.iv_slide).load("https://image.tmdb.org/t/p/w500${movieImageList[position].filePath}")
    }

    fun updateList(newList: List<MoviePoster>) {
        movieImageList = newList
        notifyDataSetChanged()
    }
}