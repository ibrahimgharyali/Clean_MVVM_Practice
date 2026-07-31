package com.ibrahimgharyali.mypracticeapplication

import com.ibrahimgharyali.mypracticeapplication.data.ProductApiService
import com.ibrahimgharyali.mypracticeapplication.data.ProductDTO
import com.ibrahimgharyali.mypracticeapplication.data.ProductRepositoryImpl
import com.ibrahimgharyali.mypracticeapplication.data.ProductsArrayDTO
import com.ibrahimgharyali.mypracticeapplication.domain.Product
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ProductRepositoryTest {

    val apiservice : ProductApiService = mock()

    @Test
    fun `Test repository success`() = runTest {
        val data = ProductsArrayDTO(listOf(
            ProductDTO(1, "A", "D", 200.0),
            ProductDTO(2, "B", "D", 50.0),
            ProductDTO(3, "C", "D", 110.0)
        ))
        // Given
        whenever(apiservice.fetchProducts()).thenReturn(data)

        val repo = ProductRepositoryImpl(apiservice)
        val products = repo.getProducts()
        val expected = listOf(
            Product(1, "A", "D", 200.0),
            Product(2, "B", "D", 50.0),
            Product(3, "C", "D", 110.0)
        )
        assertTrue(products.isSuccess)
        assertEquals(Result.success(expected), products)
    }

    @Test
    fun `Test repository fail`() = runTest {
        val exce = RuntimeException("Something wrong")
        // Given
        whenever(apiservice.fetchProducts()).thenThrow(exce)

        val repo = ProductRepositoryImpl(apiservice)
        val result = repo.getProducts()
        assertTrue(result.isFailure)
        assertEquals(exce.message, result.exceptionOrNull()?.message?:"")
    }
}