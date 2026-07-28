package com.ibrahimgharyali.mypracticeapplication.domain

import javax.inject.Inject

class ProductUseCase @Inject constructor(val repository: ProductRepository) {

    suspend fun getProductsFiltered(): Result<List<Product>> {
        val products = repository.getProducts()
        if(products.isSuccess) {
            products.onSuccess { list ->
                val filteredProducts = list.filter { product -> product.price > 100 }
                    .sortedByDescending { it.price }

                return Result.success(filteredProducts)
            }
        }
        return products
    }
}