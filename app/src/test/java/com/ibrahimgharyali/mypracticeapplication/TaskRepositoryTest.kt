package com.ibrahimgharyali.mypracticeapplication

import com.ibrahimgharyali.mypracticeapplication.data.TaskApiService
import com.ibrahimgharyali.mypracticeapplication.data.TasksDTO
import com.ibrahimgharyali.mypracticeapplication.data.TodoRepositoryImpl
import com.ibrahimgharyali.mypracticeapplication.domain.Tasks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class TaskRepositoryTest {

    val apiService : TaskApiService = mock()

    @Test
    fun `Fetch list of tasks and get list`() = runTest {
        val takslistDTO = listOf(
            TasksDTO(1, 1, "title", true),
            TasksDTO(2, 2, "title 2", false)
        )
        whenever(apiService.fetchTasks()).thenReturn(takslistDTO)

        val repository = TodoRepositoryImpl(apiService)
        val result = repository.fetchTodoList()
        val takslist = listOf(
            Tasks(1,  "title", true),
            Tasks(2, "title 2", false)
        )
        assertEquals(Result.success(takslist), result)

    }

    @Test
    fun `Fetch list of tasks throws error`() = runTest {
        val error = RuntimeException("Unable to fetch data")
        whenever(apiService.fetchTasks()).thenThrow(error)

        val repo = TodoRepositoryImpl(apiService)
        val result = repo.fetchTodoList()

        assertTrue(result.isFailure)
        assertEquals("Unable to fetch data", result.exceptionOrNull()?.message)
    }

    @Test
    fun `domain model maps from DTO fields`() {
        val dto = TasksDTO(1, 1, "test1", true)

        val domainTask = dto.toDomain()

        assertEquals(1, domainTask.id)
        assertEquals("test1", domainTask.title)
        assertEquals(true, domainTask.completed)
    }
}