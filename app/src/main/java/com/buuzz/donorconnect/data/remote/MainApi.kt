package com.buuzz.donorconnect.data.remote

import com.buuzz.donorconnect.data.model.request.PostCreateModel
import com.buuzz.donorconnect.data.model.request.UserRegisterRequestModel
import com.buuzz.donorconnect.data.model.response.GeocodeResponse
import com.buuzz.donorconnect.data.model.response.GetPostResponse
import com.buuzz.donorconnect.data.model.response.GetPostsResponse
import com.buuzz.donorconnect.data.model.response.InitContentResponse
import com.buuzz.donorconnect.data.model.response.PostRequestResponse
import com.buuzz.donorconnect.data.model.response.ResponseModel
import com.buuzz.donorconnect.data.model.response.UserDetailResponse
import com.buuzz.donorconnect.data.model.response.UserLoginResponse
import com.buuzz.donorconnect.data.model.response.UserRegisterResponse
import com.buuzz.donorconnect.data.model.response.UserRequestListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MainApi {


    @POST(ApiEndPoints.REGISTER)
    suspend fun register(
        @Body data: UserRegisterRequestModel
    ): Response<UserRegisterResponse>


    @POST(ApiEndPoints.LOG_OUT)
    suspend fun logout(
    ): Response<ResponseModel>


    @FormUrlEncoded
    @POST(ApiEndPoints.LOGIN)
    suspend fun login(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Response<UserLoginResponse>

    @GET(ApiEndPoints.USER_DETAILS)
    suspend fun fetchUserDetails(): Response<UserDetailResponse>

    @GET(ApiEndPoints.INIT_CONTENT)
    suspend fun getInitContents(): Response<InitContentResponse>

    @POST(ApiEndPoints.CREATE_POST)
    suspend fun createPost(
        @Body data: PostCreateModel
    ): Response<ResponseModel>

    @FormUrlEncoded
    @POST(ApiEndPoints.GET_ALL_POST)
    suspend fun getAllPosts(
        @Field("user_id") categoryId: String?
    ): Response<GetPostsResponse>

    @FormUrlEncoded
    @POST(ApiEndPoints.GET_POST_BY_CATEGORY)
    suspend fun getPostsByCategory(
        @Field("category_id") categoryId: String?
    ): Response<GetPostsResponse>

    @FormUrlEncoded
    @POST(ApiEndPoints.GET_POST_BY_TAG)
    suspend fun getPostsByTag(
        @Field("requiredtagids") requiredTagIds: String?
    ): Response<GetPostsResponse>


    @FormUrlEncoded
    @POST(ApiEndPoints.GET_POST)
    suspend fun getPost(
        @Field("user_id") userId: String?,
        @Field("post_id") post: String?
    ): Response<GetPostResponse>

    @FormUrlEncoded
    @POST(ApiEndPoints.REQUEST_POST)
    suspend fun requestPost(
        @Field("post_id") postId: String?,
        @Field("user_id") userId: String?
    ): Response<PostRequestResponse>

    @FormUrlEncoded
    @POST(ApiEndPoints.CANCEL_POST)
    suspend fun cancelPost(
        @Field("post_id") postId: String?,
        @Field("user_id") userId: String?
    ): Response<ResponseModel>

    @FormUrlEncoded
    @POST(ApiEndPoints.USER_REQUESTS)
    suspend fun userRequests(
        @Field("user_id") userId: String?
    ): Response<UserRequestListResponse>

    @FormUrlEncoded
    @POST(ApiEndPoints.GET_LOCATION)
    suspend fun getLocation(
        @Field("lat") latitude: Double,
        @Field("lon") longitude: Double,
    ): Response<GeocodeResponse>

    @FormUrlEncoded
    @POST(ApiEndPoints.BOOKMARK)
    suspend fun bookmark(
        @Field("post_id") postId: String?,
        @Field("user_id") userId: String?
    ): Response<ResponseModel>


    @FormUrlEncoded
    @POST(ApiEndPoints.GET_BOOKMARK_LIST)
    suspend fun getBookmarks(
        @Field("user_id") userId: String?
    ): Response<GetPostsResponse>

    @FormUrlEncoded
    @POST(ApiEndPoints.GET_RECOMMENDATION_LIST)
    suspend fun getRecommendationList(
        @Field("post_id") postId: String?
    ): Response<GetPostsResponse>


}