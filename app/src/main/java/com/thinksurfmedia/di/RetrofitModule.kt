package com.thinksurfmedia.di

import com.thinksurfmedia.backend.ApiEndpoints
import com.thinksurfmedia.backend.TsmEndpoints
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RetrofitModule {

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun apiEndpoints(): ApiEndpoints {
        val BASE_URL = "https://thinksurfmedia.herokuapp.com/api/"
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiEndpoints::class.java)
    }

    @Singleton
    @Provides
    fun tsmEndpoints(): TsmEndpoints {
        val BASE_URL = "https://thinksurfmedia.com/wp-json/"
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(TsmEndpoints::class.java)
    }
}