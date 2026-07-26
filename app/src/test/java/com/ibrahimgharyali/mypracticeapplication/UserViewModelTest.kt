package com.ibrahimgharyali.mypracticeapplication

import app.cash.turbine.test
import com.ibrahimgharyali.mypracticeapplication.domain.model.User
import com.ibrahimgharyali.mypracticeapplication.domain.repository.UserRepository
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.UiState
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.UserViewModel
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

// Use StandardTestDispatcher + mockito-kotlin + Turbine

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val repository = mock<UserRepository>()


    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun teardown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `Test fetch user and get success`() = runTest {


        val fakeUsers = listOf(User(1, "title", "test body"))
        whenever(repository.getUsers()).thenReturn(Result.success(fakeUsers))
        val viewmodel = UserViewModel(repository)
        viewmodel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Loaded(fakeUsers, false), awaitItem())
        }
    }

    @Test
    fun `Test refresh fetch user and get success`() = runTest {
        val fakeUser = listOf(User(1,"refresh test", "test data" ))
        whenever(repository.getUsers()).thenReturn(Result.success(fakeUser))
        val viewModel = UserViewModel(repository)
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Loaded(fakeUser, false), awaitItem())
            // call pull to refresh
            viewModel.loadData(true)
            assertEquals(UiState.Loaded(fakeUser, true), awaitItem())
            assertEquals(UiState.Loaded(fakeUser, false), awaitItem())
        }
    }
    @Test
    fun `Test refresh fetch user and get error`() = runTest {
        val e = RuntimeException("Unable to refresh")
        val fakeUser = listOf(User(1, "test", "refresh data"))
        whenever(repository.getUsers()).thenReturn(Result.success(fakeUser))

        val viewModel = UserViewModel(repository)
        
        // Test both snackbar event and uiState concurrently
        viewModel.snackBarEvent.test {
            viewModel.uiState.test {
                assertEquals(UiState.Loading, awaitItem())
                assertEquals(UiState.Loaded(fakeUser, false), awaitItem())

                // Mock to return a failure Result instead of throwing raw exception
                whenever(repository.getUsers()).thenReturn(Result.failure(e))
                viewModel.loadData(true)
                
                assertEquals(UiState.Loaded(fakeUser, true), awaitItem())
                assertEquals(UiState.Loaded(fakeUser, false), awaitItem())
            }
            // Assert error message emitted to snackbar flow
            assertEquals("Unable to refresh", awaitItem())
        }
    }

    @Test
    fun `Test fetch user and get failure`() = runTest {
        val error = Exception("Something went wrong")
        whenever(repository.getUsers()).thenReturn(Result.failure(error))

        val viewModel = UserViewModel(repository)
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Error(error), awaitItem())
        }
    }
}