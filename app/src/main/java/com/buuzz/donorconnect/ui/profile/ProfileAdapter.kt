package com.buuzz.donorconnect.ui.profile

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buuzz.donorconnect.R

class ProfileAdapter(
    val mlist: ArrayList<ProfileModel>,
    val activity: Activity,
    val onItemsClicked: (String) -> Unit,
) : RecyclerView.Adapter<ProfileItemView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileItemView {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_profile_item, parent, false)
        return ProfileItemView(view)
    }

    override fun onBindViewHolder(holder: ProfileItemView, position: Int) {
        val showMargin = position != (mlist.count())
        holder.setupViews(mlist[position], showMargin, position)
        holder.itemView.setOnClickListener {
            onItemsClicked.invoke(mlist[position].title)
        }
    }

    override fun getItemCount(): Int {
        return mlist.count()
    }
}