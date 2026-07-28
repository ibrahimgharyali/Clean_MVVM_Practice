package com.ibrahimgharyali.mypracticeapplication.di

import com.ibrahimgharyali.mypracticeapplication.data.ProductApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    val networkJson = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .baseUrl("https://dummyjson.com/")
        .build()


    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): ProductApiService  = retrofit.create(
        ProductApiService::class.java)
}