package com.ibrahimgharyali.mypracticeapplication

import com.ibrahimgharyali.mypracticeapplication.data.ProducrRepositoryImpl
import com.ibrahimgharyali.mypracticeapplication.data.ProductApiService
import com.ibrahimgharyali.mypracticeapplication.data.ProductArrayDTO
import com.ibrahimgharyali.mypracticeapplication.data.ProductDTO
import com.ibrahimgharyali.mypracticeapplication.domain.Product
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class Repositorytest {

    val apiService: ProductApiService = mock()

    @Test
    fun success() = runTest {
        val data = ProductArrayDTO(listOf(
            ProductDTO(1, "a", "c", 110.0),
            ProductDTO(2, "b", "c", 200.0)
        ))

        whenever(apiService.fetchroducts()).thenReturn(data)

        val repo = ProducrRepositoryImpl(apiService)
        val fetchProducts = repo.fetchProducts()
        val expectedResults = listOf(
            Product(1, "a", "c", 110.0),
            Product(2, "b", "c", 200.0)
        )
        assertEquals(Result.success(expectedResults), fetchProducts)
    }

    @Test
    fun fail() = runTest {

        val error = RuntimeException("error")

        whenever(apiService.fetchroducts()).thenThrow(error)

        val repo = ProducrRepositoryImpl(apiService)
        val fetchProducts = repo.fetchProducts()

        assertTrue(fetchProducts.isFailure)
        assertEquals(error.message, fetchProducts.exceptionOrNull()?.message)
    }
}