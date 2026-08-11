package com.ibrahimgharyali.mypracticeapplication.data

import retrofit2.http.GET
import retrofit2.http.Header

interface ProductApiService {
    @GET("products")
    suspend fun fetchProducts(@Header("Authorization") token: String): ProductArrayDTO
//    @GET("v3/football/fixtures")
//    suspend fun fetchMatchScores(@Header("Authorization") token: String): MatchScoreDTO
}