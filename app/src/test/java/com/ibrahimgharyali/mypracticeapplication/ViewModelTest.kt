package com.ibrahimgharyali.mypracticeapplication

import app.cash.turbine.test
import com.ibrahimgharyali.mypracticeapplication.domain.FilteredProductuseCase
import com.ibrahimgharyali.mypracticeapplication.domain.Product
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.theme.ProductViewModel
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.theme.UiState
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
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {
    val dispatcher = StandardTestDispatcher()
    val usecase: FilteredProductuseCase = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun success() = runTest {
        val data = listOf(
            Product(1, "a", "c", 110.0),
            Product(2, "b", "c", 200.0)
        )

        whenever(usecase.leadProducts()).thenReturn(Result.success(data))

        val viewMdo = ProductViewModel(usecase)
        viewMdo.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Loaded(data), awaitItem())
        }
    }

    @Test
    fun fail() = runTest {

        val error = RuntimeException("error")

        whenever(usecase.leadProducts()).thenReturn(Result.failure(error))

        val viewMdo = ProductViewModel(usecase)
        viewMdo.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            val state = awaitItem()
            assertTrue(state is UiState.Error)
            assertEquals(error.message, (state as UiState.Error).message)
        }
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }
}