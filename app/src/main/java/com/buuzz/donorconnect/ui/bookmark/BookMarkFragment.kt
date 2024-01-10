package com.buuzz.donorconnect.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.buuzz.donorconnect.databinding.FragmentBookMarkBinding
import com.buuzz.donorconnect.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookMarkFragment : BaseFragment() {
    private lateinit var binding: FragmentBookMarkBinding

    companion object {
        fun newInstance() = BookMarkFragment()
    }

    private val viewModel: BookMarkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookMarkBinding.inflate(layoutInflater)
        return binding.root
    }


}