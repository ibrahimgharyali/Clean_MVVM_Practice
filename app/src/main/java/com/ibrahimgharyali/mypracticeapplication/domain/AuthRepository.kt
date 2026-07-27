package com.ibrahimgharyali.mypracticeapplication.domain

interface AuthRepository {
    fun performLogin(username: String, password: String) : Result<Unit>
}