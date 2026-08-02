package com.ibrahimgharyali.mypracticeapplication

import app.cash.turbine.test
import com.ibrahimgharyali.mypracticeapplication.domain.FilteredProductuseCase
import com.ibrahimgharyali.mypracticeapplication.domain.Product
import com.ibrahimgharyali.mypracticeapplication.presentation.ProductViewModel
import com.ibrahimgharyali.mypracticeapplication.presentation.UIState
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
class ViewmodelTest {

    private val disacher = StandardTestDispatcher()
    private val usecase: FilteredProductuseCase = mock()


    @Before
    fun setup() {
        Dispatchers.setMain(disacher)
    }

    @Test
    fun success() = runTest {
        val data = listOf(Product(1, "A", "c", 110.0))

        whenever(usecase.loadFilteredProducts()).thenReturn(Result.success(data))
        val viewmodel = ProductViewModel(usecase)
        viewmodel.uistate.test {
            assertEquals(UIState.Loading, awaitItem())
            assertEquals(UIState.Loaded(data), awaitItem())
        }
    }

    @Test
    fun fail() = runTest {
        val error = RuntimeException("Susytem error")
        whenever(usecase.loadFilteredProducts()).thenReturn(Result.failure(error))

        val viewmodel = ProductViewModel(usecase)
        viewmodel.uistate.test {
            assertEquals(UIState.Loading, awaitItem())
            val state  = awaitItem()
            assertTrue(state is UIState.Error)
            assertEquals(error.message, (state as UIState.Error).message)
        }
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }
}