package com.ibrahimgharyali.mypracticeapplication

import com.ibrahimgharyali.mypracticeapplication.data.AuthRepositoryImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RepositoryTest {

    @Test
    fun `Test valid input gets success`() = runTest {
        val username = "test@test.com"
        val password = "password123"
        val repository = AuthRepositoryImpl()
        val result = repository.performLogin(username, password)
        assertEquals(Result.success(Unit), result)
    }

    @Test
    fun `Test with incorrect credentials gets error`() = runTest {
        val username = "test@tt.co"
        val password = "pass1234"
        val e = RuntimeException("Login failed. Please check username/password")
        val repository = AuthRepositoryImpl()
        val result= repository.performLogin(username, password)
        assertTrue(result.isFailure)
        assertEquals("Login failed. Please check username/password", result.exceptionOrNull()?.message)
    }
}