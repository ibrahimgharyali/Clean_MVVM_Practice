package com.ibrahimgharyali.mypracticeapplication.data

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
//        val sessionToken = runBlocking { repo.getAuthToken() }
        val newRequest = request.newBuilder()
//            .addHeader("Authorization", sessionToken)
            .build()
        return chain.proceed(newRequest)
    }
}