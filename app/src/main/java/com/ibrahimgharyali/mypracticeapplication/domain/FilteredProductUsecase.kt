package com.ibrahimgharyali.mypracticeapplication.domain

import javax.inject.Inject

class FilteredProductUsecase @Inject constructor(private val respository: ProductRepository) {

    suspend fun fetchFilteredData(minPrice: Double = 100.0): Result<List<Product>> {
        if(minPrice !in 0.0..100.0) return Result.failure(Exception("Invalid price"))
        val fetchProducts = respository.fetchProducts()
        if (fetchProducts.isSuccess) {
            fetchProducts.onSuccess { it ->
                val filteredProducts = it
                    .filter { product ->  product.price > minPrice }
                    .sortedByDescending { it.price }
                return Result.success(filteredProducts)
            }
        }
        return fetchProducts
    }

}