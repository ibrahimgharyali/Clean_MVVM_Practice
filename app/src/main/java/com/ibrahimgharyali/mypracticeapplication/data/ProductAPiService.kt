package com.ibrahimgharyali.mypracticeapplication.data

import retrofit2.http.GET

interface ProductAPiService {

    @GET("products")
    suspend fun fetchProducts(): ProductObjectDTO
}