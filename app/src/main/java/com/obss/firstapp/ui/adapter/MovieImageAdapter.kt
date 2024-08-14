package com.obss.firstapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.obss.firstapp.R
import com.obss.firstapp.data.model.movieDetail.MoviePoster
import com.obss.firstapp.utils.Constants.IMAGE_BASE_URL

class MovieImageAdapter : RecyclerView.Adapter<MovieImageAdapter.ViewHolder>() {
    private var movieImageList: List<MoviePoster> = listOf()

    inner class ViewHolder(
        val view: View,
    ) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slide_image_container, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = movieImageList.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.view.findViewById<ImageView>(R.id.iv_slide).load("$IMAGE_BASE_URL${movieImageList[position].filePath}")
        holder.view.findViewById<TextView>(R.id.tv_slide_count).text = "${position + 1}/${movieImageList.size}"
    }

    fun updateList(newList: List<MoviePoster>) {
        movieImageList = newList
        notifyDataSetChanged()
    }
}
