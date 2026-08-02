package com.ibrahimgharyali.mypracticeapplication

import com.ibrahimgharyali.mypracticeapplication.data.ProductAPiService
import com.ibrahimgharyali.mypracticeapplication.data.ProductDTO
import com.ibrahimgharyali.mypracticeapplication.data.ProductObjectDTO
import com.ibrahimgharyali.mypracticeapplication.data.ProductRepositoryImpl
import com.ibrahimgharyali.mypracticeapplication.domain.Product
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class RepositoryTest {

    private val apiservice: ProductAPiService = mock()


    @Test
    fun success() = runTest {
        val data = ProductObjectDTO(listOf(ProductDTO(1, "A", "c", 110.0)))

        whenever(apiservice.fetchProducts()).thenReturn(data)
        val repository = ProductRepositoryImpl(apiservice)
        val result = repository.fetchProducts()
        val expected = listOf(Product(1, "A", "c", 110.0))
        assertEquals(Result.success(expected), result)

    }

    @Test
    fun fail() = runTest {
        val data = ProductObjectDTO(listOf(ProductDTO(1, "A", "c", 110.0)))
        val error =  RuntimeException("Error ")
        whenever(apiservice.fetchProducts()).thenThrow(error)
        val repository = ProductRepositoryImpl(apiservice)
        val result = repository.fetchProducts()
        val expected = listOf(Product(1, "A", "c", 110.0))
        assertTrue(result.isFailure)
        assertEquals(error.message, result.exceptionOrNull()?.message?:"")

    }
}