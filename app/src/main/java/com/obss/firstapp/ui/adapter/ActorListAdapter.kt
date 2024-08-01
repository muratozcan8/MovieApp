package com.obss.firstapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.obss.firstapp.databinding.ActorItemBinding
import com.obss.firstapp.model.credit.Cast
import com.obss.firstapp.model.movieSearch.MovieSearch

class ActorListAdapter() : RecyclerView.Adapter<ActorListAdapter.ViewHolder>()  {

    private var actorList: List<Cast> = listOf()

    inner class ViewHolder(val binding: ActorItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ActorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actor = actorList[position]
        holder.binding.ivActor.load("https://image.tmdb.org/t/p/w500${actor.profilePath}")
    }

    fun updateList(newList: List<Cast>) {
        actorList = newList
        notifyDataSetChanged()
    }


}