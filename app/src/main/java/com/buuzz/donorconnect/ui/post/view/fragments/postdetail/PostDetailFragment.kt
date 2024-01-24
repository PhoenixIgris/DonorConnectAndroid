package com.buuzz.donorconnect.ui.post.view.fragments.postdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.buuzz.donorconnect.databinding.FragmentPostDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailFragment : Fragment() {


    private val viewModel: PostDetailViewModel by viewModels()
    private lateinit var binding: FragmentPostDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailBinding.inflate(layoutInflater)
        return binding.root
    }


    companion object {
        fun newInstance() = PostDetailFragment()
    }
}