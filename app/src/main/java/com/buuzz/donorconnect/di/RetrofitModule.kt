package com.buuzz.donorconnect.di


import android.annotation.SuppressLint
import android.content.Context
import com.buuzz.donorconnect.BuildConfig
import com.buuzz.donorconnect.data.local.SharedPreferencesHelper
import com.buuzz.donorconnect.data.remote.MainApi
import com.buuzz.donorconnect.di.authenticator.AccessAuthenticator
import com.buuzz.donorconnect.data.remote.TokenRefreshApi
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.buuzz.donorconnect.utils.annotation.SkipSerialisation
import javax.inject.Singleton


private const val TAG = "RetrofitModule"

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    private fun getHttpClient(
        sharedPreferencesHelper: SharedPreferencesHelper,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        appContext: Context,
        authenticator: AccessAuthenticator? = null
    ): OkHttpClient {
        val httpBuilder = OkHttpClient.Builder()
        httpBuilder.addInterceptor(httpLoggingInterceptor)
        httpBuilder.addInterceptor(
            getInterceptorWithTokenHeader(
                sharedPreferencesHelper,
                appContext
            )
        )
        authenticator?.let { httpBuilder.authenticator(authenticator) }
        return httpBuilder.build()
    }


    @Singleton
    @Provides
    fun providesRetrofit(
        @ApplicationContext appContext: Context,
        sharedPreferencesHelper: SharedPreferencesHelper,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authenticator: AccessAuthenticator,
        gson: Gson
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.API_URL)
            .client(
                getHttpClient(
                    sharedPreferencesHelper,
                    httpLoggingInterceptor,
                    appContext,
                    authenticator
                )
            )
            .build()


    @Singleton
    @Provides
    fun providesGson(): Gson {
        return GsonBuilder().addSerializationExclusionStrategy(object : ExclusionStrategy {

            override fun shouldSkipField(f: FieldAttributes): Boolean {
                return f.getAnnotation(SkipSerialisation::class.java) != null
            }

            override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                return false
            }
        }).create()
    }


    @SuppressLint("HardwareIds")
    private fun getInterceptorWithTokenHeader(
        sharedPreferencesHelper: SharedPreferencesHelper,
        appContext: Context
    ): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", sharedPreferencesHelper.accessToken ?: "")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }

    }

    @Singleton
    @Provides
    fun providesMainApi(retrofit: Retrofit): MainApi =
        retrofit.create(
            MainApi::class.java
        )





    @Singleton
    @Provides
    fun providesTokenRefreshApi(
        sharedPreferencesHelper: SharedPreferencesHelper,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @ApplicationContext appContext: Context
    ): TokenRefreshApi =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getHttpClient(sharedPreferencesHelper, httpLoggingInterceptor, appContext))
            .build()
            .create(TokenRefreshApi::class.java)

}