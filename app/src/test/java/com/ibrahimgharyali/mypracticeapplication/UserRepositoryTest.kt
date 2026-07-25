package com.ibrahimgharyali.mypracticeapplication

import com.ibrahimgharyali.mypracticeapplication.data.UserRepositoryImpl
import com.ibrahimgharyali.mypracticeapplication.data.model.UserDTO
import com.ibrahimgharyali.mypracticeapplication.data.remote.PostApiService
import com.ibrahimgharyali.mypracticeapplication.domain.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    private val api : PostApiService = mock() // both way is valid

    @Test
    fun `test repository api returns success`() = runTest {
        val mockUserDTO = listOf(UserDTO(1, 1, "test DTO", "test data"))
        whenever(api.getUsers()).thenReturn(mockUserDTO)

        val repository =  UserRepositoryImpl(api)
        val expected = listOf(User(1, "test DTO", "test data"))
        assertEquals(Result.success(expected), repository.getUsers())
    }

    @Test
    fun `test repository api returns error`() = runTest {
        val error = RuntimeException("Something went wrong")
        whenever(api.getUsers()).thenThrow(error)

        val repository = UserRepositoryImpl(api)
        val result = repository.getUsers()
        assertTrue(result.isFailure)
        assertEquals("Something went wrong", result.exceptionOrNull()?.message)
    }
}