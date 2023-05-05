package com.rittmann.datasource.network.di

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rittmann.datasource.network.api.GitHubApi
import com.rittmann.datasource.network.config.BaseRestApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.serializeNulls().create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, application: Application): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://api.github.com/")
            .client(provideOkhttpClient(application))
            .build()
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(application: Application): OkHttpClient {
        return BaseRestApi.getOkHttpClient(application)
    }

    @Provides
    @Singleton
    fun provideWeatherApi(
        retrofit: Retrofit
    ): GitHubApi {
        return retrofit.create(GitHubApi::class.java)
    }
}