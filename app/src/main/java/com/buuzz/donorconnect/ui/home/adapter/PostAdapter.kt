package com.buuzz.donorconnect.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.buuzz.donorconnect.R
import com.buuzz.donorconnect.data.model.response.Post
import com.buuzz.donorconnect.data.model.response.Tag
import com.buuzz.donorconnect.databinding.DonationListItemLytBinding
import com.buuzz.donorconnect.ui.post.ActionType
import com.buuzz.donorconnect.utils.helpers.OnActionClicked
import com.buuzz.donorconnect.utils.helpers.formatDate

class PostAdapter(
    private val postList: List<Post>,
    private val tagList: List<Tag>,
    private val callback: OnActionClicked
) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DonationListItemLytBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = postList[position]
        holder.bind(item, tagList)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    inner class ViewHolder(private val binding: DonationListItemLytBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Post, tags: List<Tag>) {
            binding.apply {
                Glide.with(binding.root.context).load(data.image)
                    .placeholder(R.drawable.donation_placeholder)
                    .into(binding.image)
                Glide.with(binding.root.context).load(data.user_image)
                    .placeholder(R.drawable.person_holder)
                    .into(binding.userImage)
                val tagNames = tags.filter { data.tag_id?.contains(it.id) == true }.map { it.name }
                tagList.text = tagNames.joinToString(" | ")
                title.text = data.title + ", This is useful for various purposes"
//                userName.text = data.user_id
                date.text = formatDate(data.created_at)
                binding.root.setOnClickListener {
                    callback.onClick(ActionType.VIEW_POST.name , data.id.toString())
                }
            }
        }
    }
}
