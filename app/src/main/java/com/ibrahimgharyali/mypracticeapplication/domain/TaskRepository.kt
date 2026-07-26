package com.ibrahimgharyali.mypracticeapplication.domain


interface TaskRepository {
    suspend fun fetchDataList(): Result<List<Tasks>>
}