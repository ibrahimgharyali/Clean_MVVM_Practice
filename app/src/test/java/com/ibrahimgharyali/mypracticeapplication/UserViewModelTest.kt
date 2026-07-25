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
            assertEquals(UiState.Loaded(fakeUsers), awaitItem())
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