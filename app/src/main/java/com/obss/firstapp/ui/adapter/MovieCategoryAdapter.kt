package com.obss.firstapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.obss.firstapp.data.model.movieDetail.Genre
import com.obss.firstapp.databinding.CategoryItemBinding

class MovieCategoryAdapter : RecyclerView.Adapter<MovieCategoryAdapter.ViewHolder>() {
    private var categoryList: List<Genre> = listOf()

    inner class ViewHolder(
        val binding: CategoryItemBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val category = categoryList[position]
        holder.binding.tvCategory.text = category.name
    }

    fun updateList(newList: List<Genre>) {
        categoryList = newList
        notifyDataSetChanged()
    }
}
