package com.ibrahimgharyali.mypracticeapplication.di

import com.ibrahimgharyali.mypracticeapplication.data.ProductAPiService
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

    @Provides
    @Singleton
    fun providesOkHTTPCLient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .build()

    private val json = Json {
        ignoreUnknownKeys = true

    }

    @Provides
    @Singleton
    fun providesRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl("https://dummyjson.com/")
        .build()

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): ProductAPiService = retrofit.create(ProductAPiService::class.java)

}