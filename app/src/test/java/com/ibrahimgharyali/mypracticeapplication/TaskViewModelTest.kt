package com.ibrahimgharyali.mypracticeapplication

import app.cash.turbine.test
import com.ibrahimgharyali.mypracticeapplication.data.TaskRepositoryImpl
import com.ibrahimgharyali.mypracticeapplication.domain.Tasks
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.UIState
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
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {

    val repository: TaskRepositoryImpl = mock()
    val dispatcher = StandardTestDispatcher()

    @Before
    fun setup(){
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun teardown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `Test viewmodel loadData gets success`() = runTest {
        val tasklist = listOf(
            Tasks(1, "title 1", true),
            Tasks(2, "title 2", false)
            )
        whenever(repository.fetchDataList()).thenReturn(Result.success(tasklist))

        val viewmodel = TaskViewModel(repository)
        viewmodel.uiState.test {
            assertEquals(UIState.Loading, awaitItem())
            assertEquals(UIState.Loaded(tasklist), awaitItem())
        }
    }

    @Test
    fun `Test viewmodel loadData gets error`() = runTest {

        val error = RuntimeException("Something went wrong")
        whenever(repository.fetchDataList()).thenReturn(Result.failure(error))

        val viewmodel = TaskViewModel(repository)
        viewmodel.uiState.test {
            assertEquals(UIState.Loading, awaitItem())
            assertEquals(UIState.Error(error), awaitItem())
        }
    }


}