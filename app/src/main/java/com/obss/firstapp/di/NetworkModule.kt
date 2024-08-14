package com.obss.firstapp.di

import com.obss.firstapp.data.network.MovieApiService
import com.obss.firstapp.utils.Constants
import com.obss.firstapp.utils.Constants.BEARER
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule
    @Inject
    constructor() {
        @Provides
        @Singleton
        fun logging(): HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        @Provides
        @Singleton
        fun client(): OkHttpClient =
            OkHttpClient
                .Builder()
                .addInterceptor { chain ->
                    val request =
                        chain
                            .request()
                            .newBuilder()
                            .addHeader("Authorization", "Bearer $BEARER")
                            .build()
                    chain.proceed(request)
                }.build()

        @Provides
        @Singleton
        fun retrofit(client: OkHttpClient): Retrofit =
            Retrofit
                .Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        @Provides
        @Singleton
        fun movieApiService(retrofit: Retrofit): MovieApiService = retrofit.create(MovieApiService::class.java)
    }
