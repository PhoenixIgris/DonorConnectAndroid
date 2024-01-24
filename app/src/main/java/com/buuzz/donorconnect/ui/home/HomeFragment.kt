package com.buuzz.donorconnect.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.buuzz.donorconnect.R
import com.buuzz.donorconnect.data.model.response.Post
import com.buuzz.donorconnect.databinding.FragmentHomeBinding
import com.buuzz.donorconnect.ui.base.BaseFragment
import com.buuzz.donorconnect.ui.home.adapter.PostAdapter
import com.buuzz.donorconnect.ui.post.ActionType
import com.buuzz.donorconnect.utils.apihelper.safeapicall.ApiCallListener
import com.buuzz.donorconnect.utils.helpers.AppLogger
import com.buuzz.donorconnect.utils.helpers.OnActionClicked
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment(), OnActionClicked {
    private lateinit var binding: FragmentHomeBinding

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setViews()
        return binding.root
    }

    private fun setViews() {
        setCategoryTabs()
        setOnClicks()
    }

    private fun setOnClicks() {

    }

    private fun setCategoryTabs() {
        viewModel.getCategoryList { tags ->
            binding.categoryList.removeAllTabs()
            binding.categoryList.addTab(
                binding.categoryList.newTab().apply {
                    text = "All"
                    tag = "All"
                }
            )
            tags.forEach { data ->
                binding.categoryList.addTab(
                    binding.categoryList.newTab().apply {
                        text = data.name
                        tag = data.id
                    }
                )
            }
            val tabs = binding.categoryList.getChildAt(0) as ViewGroup
            AppLogger.logD("POST", "$tabs")
            for (i in 0 until tabs.childCount) {

                val tab = tabs.getChildAt(i)
                val layoutParams = tab.layoutParams as LinearLayout.LayoutParams
                layoutParams.height = resources.getDimensionPixelSize(R.dimen._40dp)
                layoutParams.marginEnd = resources.getDimensionPixelSize(R.dimen._5dp)
                if (i == 0) {
                    layoutParams.marginStart = resources.getDimensionPixelSize(R.dimen._5dp)
                }
                tab.layoutParams = layoutParams
            }
            binding.categoryList.requestLayout()
        }

        binding.categoryList.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                AppLogger.logD("POST", "category ${tab?.tag.toString()}")

                fetchPostByCategory(tab?.tag.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun fetchPostByCategory(categoryId: String?) {
        if (categoryId != "All") {
            getPosts(categoryId)
        } else {
            getPosts(null)
        }
    }

    private fun getPostById(postId: String?) {
        binding.loading.isVisible = true
        viewModel.getPosts(postId, object : ApiCallListener {
            override fun onSuccess(response: String?) {
                val post = Gson().fromJson(response, Post::class.java)

                binding.loading.isVisible = false
            }

            override fun onError(errorMessage: String?) {
                binding.loading.isVisible = false
                showTopSnackBar(binding.root, errorMessage ?: "Failed to Fetch Posts")
            }

        })
    }

    private fun getPosts(categoryId: String?) {
        binding.loading.isVisible = true
        viewModel.getPosts(categoryId, object : ApiCallListener {
            override fun onSuccess(response: String?) {
                val listType = object : TypeToken<List<Post>>() {}.type
                val postList = Gson().fromJson<List<Post>>(response, listType)
                if (!postList.isNullOrEmpty()) {
                    setPostList(postList)
                }
                binding.loading.isVisible = false
            }

            override fun onError(errorMessage: String?) {
                binding.loading.isVisible = false
                showTopSnackBar(binding.root, errorMessage ?: "Failed to Fetch Posts")
            }

        })
    }

    private fun setPostList(postList: List<Post>) {
        binding.mainList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            viewModel.getTagsList { tags ->
                adapter = PostAdapter(postList, tags, this@HomeFragment)
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


}