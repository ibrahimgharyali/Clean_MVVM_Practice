package com.ibrahimgharyali.mypracticeapplication.data

import com.ibrahimgharyali.mypracticeapplication.domain.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override fun performLogin(
        username: String,
        password: String
    ): Result<Unit> =
        if(username == "test@test.com" && password == "password123")
            Result.success(Unit)
        else
            Result.failure(Exception("Login failed. Please check username/password"))

}