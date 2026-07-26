package com.ibrahimgharyali.mypracticeapplication

import com.ibrahimgharyali.mypracticeapplication.data.TaskApiService
import com.ibrahimgharyali.mypracticeapplication.data.TaskRepositoryImpl
import com.ibrahimgharyali.mypracticeapplication.data.TasksDTO
import com.ibrahimgharyali.mypracticeapplication.domain.Tasks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryTest {

    val apiService: TaskApiService = mock()


    @Test
    fun `Test repository fetched data get success `() = runTest {
        val listDto = listOf(TasksDTO(1, 1, "title", false))
        whenever(apiService.fetchTodoData()).thenReturn(listDto)

        val repo = TaskRepositoryImpl(apiService)
        val result = repo.fetchDataList()
        val domainlist = listOf(Tasks(1, "title", false))
        assertEquals(Result.success(domainlist), result)
    }

    @Test
    fun `Test repository fetched data get failure `() = runTest {
        val error = RuntimeException("Something went wrong")
        whenever(apiService.fetchTodoData()).thenThrow(error)
        val repo = TaskRepositoryImpl(apiService)
        val result = repo.fetchDataList()
        assertTrue(result.isFailure)
        assertEquals("Something went wrong", result.exceptionOrNull()?.message)
    }
}