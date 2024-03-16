package com.buuzz.donorconnect.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buuzz.donorconnect.data.model.response.Category
import com.buuzz.donorconnect.data.model.response.Tag
import com.buuzz.donorconnect.data.respository.ContentRepository
import com.buuzz.donorconnect.utils.apihelper.safeapicall.ApiCallListener
import com.buuzz.donorconnect.utils.apihelper.safeapicall.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {


    fun getUserId(onSuccess: (String?) -> Unit) {
        viewModelScope.launch {
            onSuccess(contentRepository.getUserId())

        }
    }

    fun getTagList(onSuccess: (List<Tag>) -> Unit) {
        viewModelScope.launch {
            val list = contentRepository.getTagList()
            if (list.isNotEmpty()) {
                onSuccess(list)
            }
        }
    }

    fun getCategoryList(onSuccess: (List<Category>) -> Unit) {
        viewModelScope.launch {
            val list = contentRepository.getCategoryList()
            if (list.isNotEmpty()) {
                onSuccess(list)
            }
        }
    }

    fun getPostsByCategory(categoryId: String?, apiCallListener: ApiCallListener) {
        viewModelScope.launch {
            when (val response =
                if (categoryId == null) contentRepository.getAllPosts() else contentRepository.getPostsByCategory(
                    categoryId
                )) {
                is Resource.Failure -> {
                    apiCallListener.onError(response.errorMsg)
                }

                is Resource.Success -> {
                    if (response.value.success == true) {
                        apiCallListener.onSuccess(Gson().toJson(response.value.posts))
                    } else {
                        apiCallListener.onError(response.value.message)
                    }
                }
            }
        }
    }

    fun getPostsBySearch(query: String?, apiCallListener: ApiCallListener) {
        viewModelScope.launch {
            when (val response =
                contentRepository.getPostsBySearch(
                    query
                )) {
                is Resource.Failure -> {
                    apiCallListener.onError(response.errorMsg)
                }

                is Resource.Success -> {
                    if (response.value.success == true) {
                        apiCallListener.onSuccess(Gson().toJson(response.value.posts))
                    } else {
                        apiCallListener.onError(response.value.message)
                    }
                }
            }
        }
    }

    fun getPostsByTag(tagId: String?, apiCallListener: ApiCallListener) {
        viewModelScope.launch {
            when (val response =
                if (tagId == null) contentRepository.getAllPosts() else contentRepository.getPostsByTag(
                    tagId
                )) {
                is Resource.Failure -> {
                    apiCallListener.onError(response.errorMsg)
                }

                is Resource.Success -> {
                    if (response.value.success == true) {
                        apiCallListener.onSuccess(Gson().toJson(response.value.posts))
                    } else {
                        apiCallListener.onError(response.value.message)
                    }
                }
            }
        }
    }

    fun getPostById(postId: String?, apiCallListener: ApiCallListener) {
        viewModelScope.launch {
            when (val response =
                contentRepository.getPost(postId)) {
                is Resource.Failure -> {
                    apiCallListener.onError(response.errorMsg)
                }

                is Resource.Success -> {
                    if (response.value.success == true) {
                        apiCallListener.onSuccess(Gson().toJson(response.value.data?.post_detail))
                    } else {
                        apiCallListener.onError(response.value.message)
                    }
                }
            }
        }
    }


}