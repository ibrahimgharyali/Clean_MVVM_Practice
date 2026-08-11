package com.ibrahimgharyali.mypracticeapplication

import app.cash.turbine.test
import com.ibrahimgharyali.mypracticeapplication.domain.FilteredProductUsecase
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
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ViewmodelTest {

    val dispatcher= StandardTestDispatcher()
    val useCase: FilteredProductUsecase = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun success() = runTest {
        val data = listOf(
            Product(1, "A", "c", 120.0),
            Product(2, "B", "c", 200.0),
        )
        whenever(useCase.fetchFilteredData()).thenReturn(Result.success(data))
        val viewmodel = ProductViewModel(useCase)
        viewmodel.uistate.test {
            Assert.assertEquals(UiState.Loading, awaitItem())
            Assert.assertEquals(UiState.Loaded(data), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun fail() = runTest {

        val error = RuntimeException("Error")
        whenever(useCase.fetchFilteredData()).thenReturn(Result.failure(error))
        val viewmodel = ProductViewModel(useCase)
        viewmodel.uistate.test {
            Assert.assertEquals(UiState.Loading, awaitItem())
            val state = awaitItem()

            Assert.assertTrue(state is UiState.Error)
            Assert.assertEquals(error.message, (state as UiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }


    @After
    fun teardown() {
        Dispatchers.resetMain()
    }
}