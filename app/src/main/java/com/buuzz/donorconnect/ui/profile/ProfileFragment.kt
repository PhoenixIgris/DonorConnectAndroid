package com.buuzz.donorconnect.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.buuzz.donorconnect.R
import com.buuzz.donorconnect.databinding.FragmentProfileBinding
import com.buuzz.donorconnect.ui.base.BaseFragment
import com.buuzz.donorconnect.ui.profile.ProfileItemProvider.getProfileMoreItems
import com.buuzz.donorconnect.utils.apihelper.safeapicall.ApiCallListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    private lateinit var binding: FragmentProfileBinding

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        setUpMoreView()
        return binding.root
    }

    private fun setUpMoreView() {
        binding.recyclerViewMore.layoutManager = LinearLayoutManager(context)
        val moreAdapter =
            ProfileAdapter(
                getProfileMoreItems(requireContext()),
                activity ?: Activity(),
                onItemsClicked = { onItemsClicked(it) })
        binding.recyclerViewMore.adapter = moreAdapter
    }

    private fun onItemsClicked(title: String) {
        lifecycleScope.launch {
            when (title) {
                getString(R.string.logout) -> {
                    logOut()
                }

                else -> {}
            }
        }
    }

    private fun logOut() {
        viewModel.logOut(object : ApiCallListener {
            override fun onSuccess(response: String?) {
                showTopSnackBar(binding.root, response ?: "Logged Out Successfully!!")
                requireActivity().finish()
            }

            override fun onError(errorMessage: String?) {
                showTopSnackBar(binding.root, errorMessage ?: "Error Logging Out !!")
            }

        })
    }

}