package com.ibrahimgharyali.mypracticeapplication

import com.ibrahimgharyali.mypracticeapplication.domain.FilteredProductUsecase
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
            Product(1, "A", "c", 120.0),
            Product(2, "B", "c", 200.0)
        )
        whenever(repository.fetchProducts()).thenReturn(Result.success(data))

        val usecase = FilteredProductUsecase(repository)
        val products = usecase.fetchFilteredData()
        val expected = listOf(
            Product(2, "B", "c", 200.0),
            Product(1, "A", "c", 120.0)
        )
        assertEquals(Result.success(expected), products)
    }

    @Test
    fun fail() = runTest {
        val error = RuntimeException("Error")
        whenever(repository.fetchProducts()).thenReturn(Result.failure(error))

        val repo = FilteredProductUsecase(repository)
        val products = repo.fetchFilteredData()
        assertTrue(products.isFailure)
        assertEquals(error.message, products.exceptionOrNull()?.message?:"")
    }

    @Test
    fun `no call`() = runTest {
        val result = FilteredProductUsecase(repository)
        val products = result.fetchFilteredData(-20.0)
        assertTrue(products.isFailure)
        verify(repository, never()).fetchProducts()
    }
}