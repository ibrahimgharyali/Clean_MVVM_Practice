package com.ibrahimgharyali.mypracticeapplication

import app.cash.turbine.test
import com.ibrahimgharyali.mypracticeapplication.data.TodoRepositoryImpl
import com.ibrahimgharyali.mypracticeapplication.domain.Tasks
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.UiState
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {

    val dispatcher = StandardTestDispatcher()
    val repository: TodoRepositoryImpl = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Load task list and get success`() = runTest {
        val task = listOf(Tasks(1, "title", false))
        whenever(repository.fetchTodoList()).thenReturn(Result.success(task))

        val viewmodel = TaskViewModel(repository)
        viewmodel.uistate.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Loaded(task), awaitItem())
        }
    }

    @Test
    fun `Load task list and get error`() = runTest {
        val error = RuntimeException("Cannot fetch task list")
        whenever(repository.fetchTodoList()).thenReturn(Result.failure(error))

        val viewmodel = TaskViewModel(repository)
        viewmodel.uistate.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Error(error), awaitItem() )
        }
    }
}