package com.buuzz.donorconnect.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.buuzz.donorconnect.R
import com.buuzz.donorconnect.data.model.response.Post
import com.buuzz.donorconnect.data.model.response.Tag
import com.buuzz.donorconnect.databinding.DonationListItemLytBinding
import com.buuzz.donorconnect.ui.post.ActionType
import com.buuzz.donorconnect.utils.helpers.OnActionClicked
import com.buuzz.donorconnect.utils.helpers.PostStatus
import com.buuzz.donorconnect.utils.helpers.formatDate

class PostAdapter(
    private val postList: List<Post?>,
    private val userId: String?,
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
        item?.let {
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    inner class ViewHolder(private val binding: DonationListItemLytBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Post) {
            binding.apply {
                Glide.with(binding.root.context).load(data.image)
                    .placeholder(R.drawable.donation_placeholder)
                    .into(binding.image)
                Glide.with(binding.root.context).load(data.user?.image)
                    .placeholder(R.drawable.person_placeholder)
                    .into(binding.userImage)
                val tagNames = data.tags?.map { it.name }
                tagList.text = tagNames?.joinToString(" | ") ?: ""
                title.text = data.title ?: ""
                desc.text = data.desc ?: "...."
                userName.text = data.user?.name ?: ""
                date.text = formatDate(data.created_at)
                binding.root.setOnClickListener {
                    callback.onClick(ActionType.VIEW_POST.name, data.id.toString())
                }
                if (data.request_queues?.none { it.user_id.toString() == userId } == true) {
                    if (data.pending_request_status == PostStatus.UNVERIFIED.name) {
                        status.isVisible = true
                        statusValue.text = data.pending_request_status
                    } else {
                        status.isVisible = true
                        statusValue.text = PostStatus.VERIFIED.name
                    }
                } else {
                    if (data.current_request_user_id.toString() == userId) {
                        data.status?.let {
                            status.isVisible = true
                            statusValue.text = it
                        }
                    } else {
                        data.pending_request_status?.let {
                            status.isVisible = true
                            statusValue.text = it
                        }
                    }
                }

            }
        }
    }
}
