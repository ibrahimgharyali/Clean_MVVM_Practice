package com.ibrahimgharyali.mypracticeapplication.domain

import javax.inject.Inject

class FilteredProductuseCase @Inject constructor(val repository: ProductRepository) {

    suspend fun loadFilteredProducts(minAmount: Double = 100.0): Result<List<Product>> {
        if(minAmount !in 0.0..1000.0) return Result.failure(Exception("Invalid filter"))
        val result = repository.fetchProducts()

        if (result.isSuccess) {
            result.onSuccess { products ->
                val filtered = products
                    .filter { it.amount > minAmount }
                    .sortedByDescending { it.amount }
                return Result.success(filtered)
            }
        }
        return result
    }
}