package com.ibrahimgharyali.mypracticeapplication

import com.ibrahimgharyali.mypracticeapplication.data.ProductApiService
import com.ibrahimgharyali.mypracticeapplication.data.ProductArrayDTO
import com.ibrahimgharyali.mypracticeapplication.data.ProductDTO
import com.ibrahimgharyali.mypracticeapplication.data.ProductRepositoryImpl
import com.ibrahimgharyali.mypracticeapplication.domain.Product
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class RepositoryTest {
    val apiService: ProductApiService = mock()

    @Test
    fun success() = runTest {
        val data = ProductArrayDTO(listOf(
            ProductDTO(1, "A", "c", 120.0),
            ProductDTO(2, "B", "c", 200.0),
        ))

        whenever(apiService.fetchProducts(any())).thenReturn(data)

        val repo = ProductRepositoryImpl(apiService, token= "success")
        val products = repo.fetchProducts()
        val expected = listOf(
            Product(1, "A", "c", 120.0),
            Product(2, "B", "c", 200.0),
            )
        assertEquals(Result.success(expected), products)


    }

    @Test
    fun fail() = runTest {
        val error = RuntimeException("Error")
        whenever(apiService.fetchProducts(any())).thenThrow(error)

        val repo = ProductRepositoryImpl(apiService, token="fail")
        val products = repo.fetchProducts()
        assertTrue(products.isFailure)
        assertEquals(error.message, products.exceptionOrNull()?.message?:"")


    }


    @Test(expected = CancellationException::class)
    fun `fail with cancellationExcetion`() = runTest {
        whenever(apiService.fetchProducts(any())).thenThrow(CancellationException("Cancelled"))
        val repo = ProductRepositoryImpl(apiService, token = "Cancelled")
        repo.fetchProducts()
    }
}