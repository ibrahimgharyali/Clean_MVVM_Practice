package com.ibrahimgharyali.mypracticeapplication.di

import com.ibrahimgharyali.mypracticeapplication.data.AuthInterceptor
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
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SportmonksToken

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @SportmonksToken
    fun providesSportmonksToken() : String = "h19ORBBbaoX7hi5ClgZwq4vj6xhqu5wVn86PNREiLZB4dyzdI62hL0h8o3gw"



    @Provides
    @Singleton
    fun providesOkhttpClient(authInterceptor: AuthInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .build()

    private val json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun providesRetrofitClient(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl("https://dummyjson.com/")
        .build()


    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): ProductApiService = retrofit.create(
        ProductApiService::class.java)
}