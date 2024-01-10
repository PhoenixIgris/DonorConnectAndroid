package com.buuzz.donorconnect.ui.requestlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.buuzz.donorconnect.databinding.FragmentRequestListBinding
import com.buuzz.donorconnect.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestListFragment : BaseFragment() {
    private lateinit var binding: FragmentRequestListBinding

    companion object {
        fun newInstance() = RequestListFragment()
    }

    private val viewModel: RequestListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestListBinding.inflate(layoutInflater)
        return binding.root
    }


}