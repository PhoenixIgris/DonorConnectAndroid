package com.buuzz.donorconnect.ui.profile

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.buuzz.donorconnect.databinding.RowProfileItemBinding

class ProfileItemView(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = RowProfileItemBinding.bind(itemView)

    fun setupViews(obj: ProfileModel, showMargin: Boolean, position: Int) {
        binding.tvTitle.text = obj.title
        if (obj.image.isNullOrEmpty()) {
            binding.imgTV.text = "${position + 1}"
        } else {
            Glide.with(itemView.context).load(obj.image).into(binding.img)
        }
        binding.margin.isVisible = showMargin
    }

}