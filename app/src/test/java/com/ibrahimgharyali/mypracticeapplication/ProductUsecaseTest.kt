package com.ibrahimgharyali.mypracticeapplication

import com.ibrahimgharyali.mypracticeapplication.domain.Product
import com.ibrahimgharyali.mypracticeapplication.domain.ProductRepository
import com.ibrahimgharyali.mypracticeapplication.domain.ProductUseCase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ProductUseCaseTest {

    // filter it. price > something
    // sort list descending


    private val repository: ProductRepository= mock()
    private lateinit var useCase: ProductUseCase

    @Before
    fun setup() {
        useCase = ProductUseCase(repository)
    }

    @Test
    fun `getProductsFiltered returns and sorted products on success`() = runTest {
        // Given
        val product1 = Product(1, "test 1", "desc 1", 10.0)
        val product2 = Product(2, "test 2", "desc 2", 100.0)
        val product3 = Product(3, "test 3", "desc 3", 50.0)
        val product4 = Product(4, "test 4", "desc 4", 500.0)
        val product5 = Product(5, "test 5", "desc 5", 1120.0)
        val product6 = Product(6, "test 6", "desc 6", 30.0)


        val rawList = listOf(product1, product2, product3, product4, product5, product6)
        whenever(repository.getProducts()).thenReturn(Result.success(rawList))
        // When
        val res =  useCase.getProductsFiltered()
        // Then
        assertTrue(res.isSuccess)
        val filteredList = res.getOrNull()!!
        assertEquals(2, filteredList.size)
        assertEquals(1120.0, filteredList[0].price, Double.NaN)
        assertEquals(500.0, filteredList[1].price, Double.NaN)

    }

    @Test
    fun `getProductsFiltered returns failure when repoistory fails`() = runTest {
        // Given
        val exce = RuntimeException("Network error")
        whenever(repository.getProducts()).thenReturn(Result.failure(exce))

        // When
        val res =  useCase.getProductsFiltered()
        // Then
        assertTrue(res.isFailure)
        assertEquals(exce, res.exceptionOrNull())
    }
    @Test
    fun `getProductsFiltered returns never when repository does not call`() = runTest {
        // Given
        val exce = Exception("Incorrect min price")

        // When
        val res =  useCase.getProductsFiltered(-20.0)
        // Then
        assertTrue(res.isFailure)
        verify(repository, never()).getProducts()
        assertEquals(exce.message, res.exceptionOrNull()?.message)
    }


    @After
    fun teardown() {

    }
}