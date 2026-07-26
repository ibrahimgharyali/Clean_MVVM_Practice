package com.ibrahimgharyali.mypracticeapplication.data

import com.ibrahimgharyali.mypracticeapplication.domain.Tasks
import com.ibrahimgharyali.mypracticeapplication.domain.TodoRepository

import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(private val apiService: TaskApiService): TodoRepository {

    override suspend fun fetchTodoList(): Result<List<Tasks>> = try {
        val result = apiService.fetchTasks().map { it.toDomain() }
        Result.success(result)
    }
    catch (e: Exception) {
        Result.failure(e)
    }
}