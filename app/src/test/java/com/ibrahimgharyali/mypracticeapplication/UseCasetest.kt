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
            Product(1, "a", "c", 110.0),
            Product(2, "b", "c", 200.0)
        )
        whenever(repository.fetchProducts()).thenReturn(Result.success(data))

        val usecase = FilteredProductuseCase(repository)
        val fetchProducts = usecase.leadProducts()
        val expected = listOf(
            Product(2, "b", "c", 200.0),
            Product(1, "a", "c", 110.0)
        )
        assertEquals(Result.success(expected), fetchProducts)
    }

    @Test
    fun fail() = runTest {

        val error = RuntimeException("error")

        whenever(repository.fetchProducts()).thenReturn(Result.failure(error))

        val usecase = FilteredProductuseCase(repository)
        val fetchProducts = usecase.leadProducts()

        assertTrue(fetchProducts.isFailure)
        assertEquals(error.message, fetchProducts.exceptionOrNull()?.message)
    }

    @Test
    fun `fail no call`() = runTest {

        val usecase = FilteredProductuseCase(repository)
        usecase.leadProducts(-20.0)
        verify(repository, never()).fetchProducts()
    }
}