package com.buuzz.donorconnect.data.respository

import com.buuzz.donorconnect.data.local.DataStoreHelper
import com.buuzz.donorconnect.data.local.SharedPreferencesHelper
import com.buuzz.donorconnect.data.model.request.PostCreateModel
import com.buuzz.donorconnect.data.model.response.Category
import com.buuzz.donorconnect.data.model.response.GetPostsResponse
import com.buuzz.donorconnect.data.model.response.InitContentData
import com.buuzz.donorconnect.data.model.response.ResponseModel
import com.buuzz.donorconnect.data.model.response.Tag
import com.buuzz.donorconnect.data.remote.MainApi
import com.buuzz.donorconnect.utils.apihelper.safeapicall.Resource
import com.buuzz.donorconnect.utils.apihelper.safeapicall.SafeApiCall
import com.buuzz.donorconnect.utils.helpers.AppData
import com.buuzz.donorconnect.utils.helpers.AppLogger
import com.google.gson.Gson
import javax.inject.Inject

private const val TAG = "ContentRepository"

class ContentRepository @Inject constructor(
    private val mainApi: MainApi,
    private val dataStoreHelper: DataStoreHelper,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) {

    suspend fun createPost(
        image: String?,
        title: String,
        desc: String,
        category_id: Int?,
        tag_id: List<Int>?,
    ): Resource<ResponseModel> {
        val data = PostCreateModel(image =  image,
        title = title, desc =desc, category_id= category_id, tag_id= tag_id, user_id = dataStoreHelper.readStringFromDatastore(AppData.USER_ID))
        val response = SafeApiCall.execute {
            mainApi.createPost(
                data
            )
        }
        when (response) {
            is Resource.Success -> {
                AppLogger.logD(TAG, "image uploaded")
            }

            is Resource.Failure -> {

            }
        }
        return response
    }


    suspend fun getInitContents() {
        when (val response = SafeApiCall.execute { mainApi.getInitContents() }) {
            is Resource.Failure -> {
                AppLogger.logD(TAG, response.errorMsg)
            }

            is Resource.Success -> {
                if (response.value.success && !response.value.data?.tags.isNullOrEmpty()) {
                    dataStoreHelper.saveStringToDatastore(
                        AppData.INIT_CONTENTS to
                                Gson().toJson(response.value.data)
                    )
                }
            }
        }
    }

    suspend fun getTagList(): List<Tag> {
        val data = Gson().fromJson(
            dataStoreHelper.readStringFromDatastore(AppData.INIT_CONTENTS),
            InitContentData::class.java
        )
        return data.tags ?: emptyList()
    }

    suspend fun getCategoryList(): List<Category> {
        val data = Gson().fromJson(
            dataStoreHelper.readStringFromDatastore(AppData.INIT_CONTENTS),
            InitContentData::class.java
        )
        return data.categories ?: emptyList()
    }

    suspend fun getPostsByCategory(category_id: String?) : Resource<GetPostsResponse>{
        return SafeApiCall.execute { mainApi.getPostsByCategory(category_id) }
    }

    suspend fun getAllPosts() : Resource<GetPostsResponse>{
        return SafeApiCall.execute { mainApi.getAllPosts(dataStoreHelper.readStringFromDatastore(AppData.USER_ID)) }
    }

    suspend fun getPost(post_id: String?) : Resource<GetPostsResponse>{
        return SafeApiCall.execute { mainApi.getPostsByCategory(post_id) }
    }

}