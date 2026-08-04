package com.ibrahimgharyali.mypracticeapplication.data

import retrofit2.http.GET

interface ProductApiService {
    @GET("products")
    suspend fun fetchroducts(): ProductArrayDTO
}