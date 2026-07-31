package com.ibrahimgharyali.mypracticeapplication.domain

import javax.inject.Inject

class ProductUseCase @Inject constructor(private val repository: ProductRepository) {

    suspend fun getProductsFiltered(minPrice: Double = 100.0): Result<List<Product>> {
        if (minPrice !in 0.0..1000.0) return Result.failure(Exception("Incorrect min price"))
        val products = repository.getProducts()
        if(products.isSuccess) {
            products.onSuccess { list ->
                val filteredProducts = list.filter { product -> product.price > minPrice }
                    .sortedByDescending { it.price }

                return Result.success(filteredProducts)
            }
        }
        return products
    }
}