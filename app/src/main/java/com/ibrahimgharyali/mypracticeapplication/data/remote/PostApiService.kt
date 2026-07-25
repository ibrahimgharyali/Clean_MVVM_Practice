package com.ibrahimgharyali.mypracticeapplication.data.remote

import com.ibrahimgharyali.mypracticeapplication.data.model.UserDTO
import retrofit2.http.GET

interface PostApiService {

    @GET("posts")
    suspend fun getUsers(): List<UserDTO>
}