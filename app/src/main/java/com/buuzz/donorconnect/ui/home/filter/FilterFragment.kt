package com.buuzz.donorconnect.ui.home.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.buuzz.donorconnect.R
import com.buuzz.donorconnect.data.model.response.Tag
import com.buuzz.donorconnect.databinding.FragmentFilterBinding
import com.buuzz.donorconnect.ui.post.ActionType
import com.buuzz.donorconnect.ui.post.adapter.TagAdapter
import com.buuzz.donorconnect.utils.helpers.AppLogger
import com.buuzz.donorconnect.utils.helpers.OnActionClicked
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val TAG_LIST = "tag_list"

class FilterFragment : DialogFragment() {

    private lateinit var binding: FragmentFilterBinding
    private var callback: OnActionClicked? = null
    private var tagAdapter: TagAdapter? = null
    private var tagList: List<Tag> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val data = it.getString(TAG_LIST)
            val listType = object : TypeToken<List<Tag>>() {}.type
            tagList = Gson().fromJson(data, listType)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(layoutInflater)
        tagAdapter = TagAdapter(tagList) { selectedTag ->
            AppLogger.logD("POST", "selectedTags $selectedTag")
        }

        val layoutManager = FlexboxLayoutManager(requireContext())
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        binding.tagList.layoutManager = layoutManager
        binding.tagList.adapter = tagAdapter
        binding.applyBtn.setOnClickListener {
            callback?.onClick(
                ActionType.APPLY_FILTER.name,
                Gson().toJson(tagAdapter?.getSelectedTags())
            )
            this.dialog?.dismiss()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // Set the dialog width to 80% of the screen width
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        dialog?.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    fun setCallback(callbackListener: OnActionClicked) {
        this.callback = callbackListener
    }

    companion object {
        @JvmStatic
        fun newInstance(tagList: List<Tag>) =
            FilterFragment().apply {
                arguments = Bundle().apply {
                    putString(TAG_LIST, Gson().toJson(tagList))
                }
            }
    }
}