package com.buuzz.donorconnect.ui.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.buuzz.donorconnect.data.model.response.GetPostsResponse
import com.buuzz.donorconnect.data.model.response.Post
import com.buuzz.donorconnect.data.model.response.UserRequestListResponse
import com.buuzz.donorconnect.databinding.FragmentBookMarkBinding
import com.buuzz.donorconnect.ui.base.BaseFragment
import com.buuzz.donorconnect.ui.home.adapter.PostAdapter
import com.buuzz.donorconnect.ui.post.ActionType
import com.buuzz.donorconnect.ui.post.view.PostActivity
import com.buuzz.donorconnect.utils.apihelper.safeapicall.ApiCallListener
import com.buuzz.donorconnect.utils.helpers.IntentParams
import com.buuzz.donorconnect.utils.helpers.OnActionClicked
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookMarkFragment : BaseFragment(), OnActionClicked {
    private lateinit var binding: FragmentBookMarkBinding

    companion object {
        fun newInstance() = BookMarkFragment()
    }

    private val viewModel: BookMarkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookMarkBinding.inflate(layoutInflater)
        fetchList()
        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchList()
        }
        return binding.root
    }

    private fun fetchList() {
        binding.swipeRefreshLayout.isRefreshing = true
        viewModel.getBookmarkList(object : ApiCallListener {
            override fun onSuccess(response: String?) {
                val data = Gson().fromJson(response, GetPostsResponse::class.java)
                data.posts?.let { setPostList(it) }
                binding.swipeRefreshLayout.isRefreshing = false
            }

            override fun onError(errorMessage: String?) {
                binding.swipeRefreshLayout.isRefreshing = false
                showTopSnackBar(binding.root, errorMessage ?: "Error fetching your bookmark lists")
            }

        })
    }


    private fun setPostList(postList: List<Post?>) {
        binding.mainList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            viewModel.getUserId { userId ->
                adapter = PostAdapter(postList, userId, this@BookMarkFragment)
            }
        }

    }

    override fun onClick(type: String, value: String?) {
        when (type) {
            ActionType.VIEW_POST.name -> {
                getPostById(postId = value)
            }
        }
    }

    private fun getPostById(postId: String?) {
        binding.loading.isVisible = true
        viewModel.getPostById(postId, object : ApiCallListener {
            override fun onSuccess(response: String?) {
                binding.loading.isVisible = false
                val intent = Intent(requireContext(), PostActivity::class.java)
                intent.putExtra(IntentParams.POST_DETAIL, response)
                startActivity(intent)
            }

            override fun onError(errorMessage: String?) {
                binding.loading.isVisible = false
                showTopSnackBar(binding.root, errorMessage ?: "Failed to Fetch Posts")
            }

        })
    }

}