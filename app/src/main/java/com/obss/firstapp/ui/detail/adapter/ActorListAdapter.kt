package com.obss.firstapp.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.obss.firstapp.data.model.credit.Cast
import com.obss.firstapp.databinding.ActorItemBinding
import com.obss.firstapp.utils.Constants.IMAGE_BASE_URL

class ActorListAdapter : RecyclerView.Adapter<ActorListAdapter.ViewHolder>() {
    private var actorList: List<Cast> = listOf()

    inner class ViewHolder(
        val binding: ActorItemBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = ActorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = actorList.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val actor = actorList[position]
        if (!actor.profilePath.isNullOrEmpty()) {
            holder.binding.ivActor.load("$IMAGE_BASE_URL${actor.profilePath}")
        } else {
            if (actor.gender == MALE) {
                holder.binding.ivActor.setImageResource(com.obss.firstapp.R.drawable.face_male_24)
            } else if (actor.gender == FEMALE) {
                holder.binding.ivActor.setImageResource(com.obss.firstapp.R.drawable.face_female_24)
            }
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(actor) }
        }
    }

    private var onItemClickListener: ((Cast) -> Unit)? = null

    fun setOnItemClickListener(listener: (Cast) -> Unit) {
        onItemClickListener = listener
    }

    fun updateList(newList: List<Cast>) {
        actorList = newList
        notifyDataSetChanged()
    }

    companion object {
        private const val FEMALE = 1
        private const val MALE = 2
    }
}
