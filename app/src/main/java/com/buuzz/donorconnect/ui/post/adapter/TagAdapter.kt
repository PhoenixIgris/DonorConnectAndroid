package com.buuzz.donorconnect.ui.post.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.buuzz.donorconnect.R
import com.buuzz.donorconnect.data.model.response.Tag
import com.google.android.material.card.MaterialCardView

class TagAdapter(private val tags: List<Tag>, private val onTagClickListener: (Tag) -> Unit) :
    RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    private val selectedTags = mutableSetOf<Int>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: MaterialCardView = itemView.findViewById(R.id.card)
        val tagName: TextView = itemView.findViewById(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tag = tags[position]

        // Set tag name
        holder.tagName.text = tag.name

        // Set click listener for card view
        holder.cardView.setOnClickListener {
            toggleTagSelection(tag)
            onTagClickListener(tag)
            notifyItemChanged(holder.adapterPosition)
        }

        // Change color based on selection state
        if (selectedTags.contains(tag.id)) {
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.primaryLightColor
                )
            )
            holder.tagName.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        } else {
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.card_background_gray
                )
            )
            holder.tagName.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    private fun toggleTagSelection(tag: Tag) {
        if (selectedTags.contains(tag.id)) {
            selectedTags.remove(tag.id)
        } else {
            selectedTags.add(tag.id)
        }
    }

    fun getSelectedTags(): List<Int> {
        return selectedTags.toList()
    }
}
