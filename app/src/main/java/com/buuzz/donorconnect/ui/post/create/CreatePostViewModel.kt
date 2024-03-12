package com.buuzz.donorconnect.ui.post.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buuzz.donorconnect.data.model.response.Category
import com.buuzz.donorconnect.data.model.response.Tag
import com.buuzz.donorconnect.data.respository.ContentRepository
import com.buuzz.donorconnect.utils.apihelper.safeapicall.ApiCallListener
import com.buuzz.donorconnect.utils.apihelper.safeapicall.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {


    fun getTagsList(onSuccess: (List<Tag>) -> Unit) {
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


    fun createPost(
        image: String?,
        title: String,
        desc: String,
        category_id: Int?,
        tag_id: List<Int>?,
        address : String,
        lat : Double,
        lng : Double,
        callback: ApiCallListener
    ) {
        viewModelScope.launch {
            when (val response =
                contentRepository.createPost(
                    image,
                    title,
                    desc,
                    category_id,
                    tag_id, address, lat, lng
                )
            ) {
                is Resource.Failure -> {
                    callback.onError(response.errorMsg)
                }

                is Resource.Success -> {
                    callback.onSuccess(response.value.message)
                }
            }
        }

    }

    fun getLocation(apiCallListener: ApiCallListener, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            when (val response = contentRepository.getLocation(latitude, longitude)) {
                is Resource.Failure -> apiCallListener.onError(response.errorMsg)
                is Resource.Success -> apiCallListener.onSuccess(response.value.display_name ?: "")
            }
        }

    }
}