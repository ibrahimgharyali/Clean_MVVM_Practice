package com.ibrahimgharyali.mypracticeapplication.domain


interface CustomRepository {
    suspend fun fetchDataList(): Result<List<Tasks>>
}