package com.ibrahimgharyali.mypracticeapplication

import app.cash.turbine.test
import com.ibrahimgharyali.mypracticeapplication.domain.Product
import com.ibrahimgharyali.mypracticeapplication.domain.ProductUseCase
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.theme.ProdState
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.theme.ProductViewModel
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
class ProductViewModelTest {

    private val dispatcher = StandardTestDispatcher()


    private val usecase: ProductUseCase = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Fetch products success`() = runTest {
        val data = listOf(Product(1, "A", "D", 200.0),
//            Product(2, "B", "D", 50.0),
            Product(3, "C", "D", 110.0)
        )
        // Given
        whenever(usecase.getProductsFiltered()).thenReturn(Result.success(data))

        // when
        val viewmdoel = ProductViewModel(usecase)
        val expected = listOf(Product(1, "A", "D", 200.0),
            Product(3, "C", "D", 110.0)
            )

        // Then
        viewmdoel.uiState.test {
            assertEquals(ProdState.Loading, awaitItem())
            assertEquals(ProdState.Loaded(expected), awaitItem())
        }
    }

    @Test
    fun `Fetch product failure`() = runTest {
        val exd = RuntimeException("Smething woring")
        whenever(usecase.getProductsFiltered()).thenReturn(Result.failure(exd))

        // then
        val viewmdoel = ProductViewModel(usecase)
        viewmdoel.uiState.test {
            assertEquals(ProdState.Loading, awaitItem())
            val state = awaitItem()
            assertTrue(state is ProdState.Error)
            assertEquals(exd.message, (state as ProdState.Error).message)
        }


    }

}