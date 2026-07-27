package com.ibrahimgharyali.mypracticeapplication

import app.cash.turbine.test
import com.ibrahimgharyali.mypracticeapplication.domain.AuthRepository
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.LoginViewModel
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModeltest {

    private val dispatcher = StandardTestDispatcher()
    private val repository: AuthRepository = mock()

    @Before
    fun setup(){
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Test perform login gets success`() = runTest {
        whenever(repository.performLogin("test@test.com", "password123"))
            .thenReturn(Result.success(Unit))

        val viewmdoel = LoginViewModel(repository)

        viewmdoel.uiState.test {
            assertTrue(awaitItem() is UIState.Idle)
            viewmdoel.performLogin("test@test.com", "password123")
            assertEquals(UIState.Loading, awaitItem())
            assertEquals(UIState.Success, awaitItem())
        }
    }

    @Test
    fun `Test perform login with invalid input, shows inline error`() = runTest {
        val viewModel = LoginViewModel(repository)
        viewModel.uiState.test {
            assertTrue(awaitItem() is UIState.Idle)
            viewModel.performLogin("test@t", "pa23")
            assertEquals(UIState.Loading, awaitItem())
            assertEquals(UIState.Idle("Invalid email", "Password must be at least 6 characters"), awaitItem())
            verify(repository, never()).performLogin(any(), any())
        }
    }

    @Test
    fun `incorrect credentials throws error`() = runTest {

    }
}