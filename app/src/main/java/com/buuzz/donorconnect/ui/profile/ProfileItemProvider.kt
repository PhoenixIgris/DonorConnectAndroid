package com.buuzz.donorconnect.ui.profile

import android.content.Context
import com.buuzz.donorconnect.R

object ProfileItemProvider {
    fun getProfileMoreItems(context: Context): ArrayList<ProfileModel> {
        val moreProfileArray = ArrayList<ProfileModel>()
        moreProfileArray.clear()
        moreProfileArray.add(
            ProfileModel(
                context.resources.getString(R.string.logout),
            )
        )
        return moreProfileArray
    }

}