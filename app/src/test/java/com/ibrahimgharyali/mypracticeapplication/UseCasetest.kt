package com.ibrahimgharyali.mypracticeapplication

import com.ibrahimgharyali.mypracticeapplication.domain.FilteredProductuseCase
import com.ibrahimgharyali.mypracticeapplication.domain.Product
import com.ibrahimgharyali.mypracticeapplication.domain.ProductRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class UseCasetest {

    val repository: ProductRepository = mock()

    @Test
    fun success() = runTest {
        val data = listOf(
            Product(1, "A", "c", 110.0),
            Product(2, "B", "c", 50.0),
            Product(3, "C", "c", 200.0)
        )

        whenever(repository.fetchProducts()).thenReturn(Result.success(data))
        val uscase = FilteredProductuseCase(repository)
        val result = uscase.loadFilteredProducts()
        val expected = listOf(Product(3, "C", "c", 200.0),
            Product(1, "A", "c", 110.0))
        assertEquals(Result.success(expected), result)

    }

    @Test
    fun fail() = runTest {
        val error =  RuntimeException("Error ")
        whenever(repository.fetchProducts()).thenReturn(Result.failure(error))
        val uscase = FilteredProductuseCase(repository)
        val result = uscase.loadFilteredProducts()
//        val expected = listOf(Product(1, "A", "c", 110.0))
        assertTrue(result.isFailure)
        assertEquals(error.message, result.exceptionOrNull()?.message?:"")

    }

    @Test
    fun `fail not run`() = runTest {
        val error =  RuntimeException("Invalid filter")

        val uscase = FilteredProductuseCase(repository)
        val result = uscase.loadFilteredProducts(-20.0)
        assertTrue(result.isFailure)
        assertEquals(error.message, result.exceptionOrNull()?.message?:"")

        verify(repository, never()).fetchProducts()

    }
}