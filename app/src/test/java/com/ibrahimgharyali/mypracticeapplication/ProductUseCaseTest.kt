package com.ibrahimgharyali.mypracticeapplication

import com.ibrahimgharyali.mypracticeapplication.domain.Product
import com.ibrahimgharyali.mypracticeapplication.domain.ProductRepository
import com.ibrahimgharyali.mypracticeapplication.domain.ProductUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ProductUseCaseTest {

    val repository : ProductRepository = mock()
    lateinit var usecase : ProductUseCase

    @Before
    fun setUp() {
        usecase = ProductUseCase(repository)
    }

    @Test
    fun success() = runTest {
        val data = listOf(Product(1, "A", "D", 200.0),
            Product(2, "B", "D", 50.0),
            Product(3, "C", "D", 110.0)
        )
        // Given
        whenever(repository.getProducts()).thenReturn(Result.success(data))

        // when
        val usecase = ProductUseCase(repository)
        val result = usecase.getProductsFiltered()

        // Then
        val expected = listOf(Product(1, "A", "D", 200.0),
            Product(3, "C", "D", 110.0))

        assertEquals(Result.success(expected), result)

    }

    @Test
    fun failure() = runTest {
        val exception = RuntimeException("Something wromg")
        // Given
        whenever(repository.getProducts()).thenReturn(Result.failure(exception))

        // when
        val usecase = ProductUseCase(repository)
        val result = usecase.getProductsFiltered()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception.message, result.exceptionOrNull()?.message)

    }
    @Test
    fun `not executed`() = runTest {
        // when
        val usecase = ProductUseCase(repository)
        val result = usecase.getProductsFiltered(-20.0)

        // Then
        verify(repository, never()).getProducts()
        assertTrue(result.isFailure)

    }
}