package com.ibrahimgharyali.mypracticeapplication.data

import com.ibrahimgharyali.mypracticeapplication.domain.TaskRepository
import com.ibrahimgharyali.mypracticeapplication.domain.Tasks
import javax.inject.Inject


class TaskRepositoryImpl @Inject constructor(private val apiservice: TaskApiService) : TaskRepository {

    override suspend fun fetchDataList(): Result<List<Tasks>> = try {
        val tasks = apiservice.fetchTodoData().map { it.toDomain() }
        Result.success(tasks)
    }
    catch (e : Exception) {
        Result.failure(e)
    }
}