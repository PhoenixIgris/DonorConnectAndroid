package com.buuzz.donorconnect.ui.bookmark

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
class BookMarkViewModel @Inject constructor(
    private val contentRepository: ContentRepository
): ViewModel() {

    fun getUserId(onSuccess: (String?) -> Unit) {
        viewModelScope.launch {
            onSuccess(contentRepository.getUserId())

        }
    }


    fun getBookmarkList(callback: ApiCallListener) {
        viewModelScope.launch {
            when (val response = contentRepository.getBookmarkList()) {
                is Resource.Failure -> {
                    callback.onError(response.errorMsg)
                }

                is Resource.Success -> {
                    if (response.value.success == true) {
                        callback.onSuccess(Gson().toJson(response.value))
                    } else {
                        callback.onError(response.value.message)
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