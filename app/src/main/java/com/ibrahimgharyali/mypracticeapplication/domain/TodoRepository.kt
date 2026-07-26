package com.ibrahimgharyali.mypracticeapplication.domain

interface TodoRepository {
    suspend fun fetchTodoList(): Result<List<Tasks>>
}