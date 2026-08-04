package com.ibrahimgharyali.mypracticeapplication.domain

import javax.inject.Inject

class FilteredProductuseCase @Inject constructor(private val repository: ProductRepository) {

    suspend fun leadProducts(minPrice: Double = 100.0): Result<List<Product>> {
        if(minPrice !in 0.0..10000.0) return Result.failure(Exception("Invalid price"))
        val fetchProducts = repository.fetchProducts()
        if (fetchProducts.isSuccess) {
            fetchProducts.onSuccess {
                return Result.success(it.filter { product -> product.price >= minPrice }
                    .sortedByDescending { it.price })
            }
        }
        return fetchProducts
    }
}