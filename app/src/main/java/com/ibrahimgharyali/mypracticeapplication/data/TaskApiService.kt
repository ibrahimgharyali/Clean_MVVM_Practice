package com.ibrahimgharyali.mypracticeapplication.data

import retrofit2.http.GET

interface TaskApiService {

    @GET("todos")
    suspend fun fetchTasks(): List<TasksDTO>
}