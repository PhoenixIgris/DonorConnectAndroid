package com.buuzz.donorconnect.ui.post.view.fragments.postdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buuzz.donorconnect.data.respository.ContentRepository
import com.buuzz.donorconnect.utils.apihelper.safeapicall.ApiCallListener
import com.buuzz.donorconnect.utils.apihelper.safeapicall.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {


    fun requestPost(callback: ApiCallListener, postId: String) {
        viewModelScope.launch {
            when (val response = contentRepository.requestPost(postId)) {
                is Resource.Failure -> {
                    callback.onError(response.errorMsg)
                }

                is Resource.Success -> {
                    callback.onSuccess(Gson().toJson(response.value))
                }
            }
        }
    }


    fun cancelPost(callback: ApiCallListener, postId: String) {
        viewModelScope.launch {
            when (val response = contentRepository.cancelPost(postId)) {
                is Resource.Failure -> {
                    callback.onError(response.errorMsg)
                }

                is Resource.Success -> {
                    callback.onSuccess(Gson().toJson(response.value))
                }
            }
        }
    }

    fun bookmarkUnBookmarkPost(callback: ApiCallListener, postId: String) {
        viewModelScope.launch {
            when (val response = contentRepository.bookmarkPost(
                    postId
                )) {
                is Resource.Failure -> {
                    callback.onError(response.errorMsg)
                }

                is Resource.Success -> {
                    callback.onSuccess(Gson().toJson(response.value))
                }
            }
        }
    }

    fun getRecommendationList(callback: ApiCallListener, postId: String) {
        viewModelScope.launch {
            when (val response = contentRepository.getRecommendationList(
                postId
            )) {
                is Resource.Failure -> {
                    callback.onError(response.errorMsg)
                }

                is Resource.Success -> {
                    callback.onSuccess(Gson().toJson(response.value))
                }
            }
        }
    }

    fun getUserId(onSuccess: (String?) -> Unit) {
        viewModelScope.launch {
            onSuccess(contentRepository.getUserId())

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