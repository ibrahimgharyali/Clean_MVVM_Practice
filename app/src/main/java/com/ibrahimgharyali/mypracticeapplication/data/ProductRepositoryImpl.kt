package com.ibrahimgharyali.mypracticeapplication.data

import com.ibrahimgharyali.mypracticeapplication.domain.Product
import com.ibrahimgharyali.mypracticeapplication.domain.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(val apiService: ProductApiService) : ProductRepository {

    override suspend fun getProducts(): Result<List<Product>>  = try{

        val products = apiService.fetchProducts().products.map<ProductDTO, Product> { prodDto->
            prodDto.toDomain()
        }
        Result.success(products)
    }
    catch (e: Exception) {
        Result.failure(Exception(e))
    }
}