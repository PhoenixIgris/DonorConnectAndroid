package com.buuzz.donorconnect.ui.post.view.fragments.postdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.buuzz.donorconnect.data.model.response.Post
import com.buuzz.donorconnect.databinding.FragmentPostDetailBinding
import com.buuzz.donorconnect.ui.base.BaseFragment
import com.buuzz.donorconnect.utils.helpers.AppLogger
import com.buuzz.donorconnect.utils.helpers.IntentParams
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailFragment : BaseFragment() {


    private val viewModel: PostDetailViewModel by viewModels()
    private lateinit var binding: FragmentPostDetailBinding
    private var postDetail: Post? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppLogger.logD("POST", requireActivity().intent.getStringExtra(IntentParams.POST_DETAIL))
        postDetail = Gson().fromJson(
            requireActivity().intent.getStringExtra(IntentParams.POST_DETAIL),
            Post::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailBinding.inflate(layoutInflater)
        setView()
        return binding.root
    }

    private fun setView() {
        binding.apply {

        }
    }


    companion object {
        fun newInstance() = PostDetailFragment()
    }
}