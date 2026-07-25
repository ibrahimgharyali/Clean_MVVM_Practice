package com.ibrahimgharyali.mypracticeapplication.domain.repository

import com.ibrahimgharyali.mypracticeapplication.domain.model.User

interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
}