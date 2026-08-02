package com.ibrahimgharyali.mypracticeapplication.data

import com.ibrahimgharyali.mypracticeapplication.domain.Product
import com.ibrahimgharyali.mypracticeapplication.domain.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val apiService: ProductAPiService) : ProductRepository{
    override suspend fun fetchProducts(): Result<List<Product>> = try {
        val data = apiService.fetchProducts().products.map {
            Product(
                it.id,
                it.title,
                it.description,
                it.price
            )
        }

        Result.success(data)
    }catch (e: Exception) {
        Result.failure(e)
    }

}