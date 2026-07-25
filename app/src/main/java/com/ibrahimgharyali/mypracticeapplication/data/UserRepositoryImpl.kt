package com.ibrahimgharyali.mypracticeapplication.data

import com.ibrahimgharyali.mypracticeapplication.data.remote.PostApiService
import com.ibrahimgharyali.mypracticeapplication.domain.model.User
import com.ibrahimgharyali.mypracticeapplication.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor (
    private  val api: PostApiService
) : UserRepository {

    override suspend fun getUsers(): Result<List<User>> = try {
        val posts = api.getUsers().map { dto ->
            User(id = dto.id, title = dto.title, body = dto.body)
        }
        Result.success(posts)
    }
    catch (e: Exception){
        Result.failure(e)
    }
}