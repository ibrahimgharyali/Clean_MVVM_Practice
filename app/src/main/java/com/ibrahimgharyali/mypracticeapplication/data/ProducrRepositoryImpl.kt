package com.ibrahimgharyali.mypracticeapplication.data

import com.ibrahimgharyali.mypracticeapplication.domain.Product
import com.ibrahimgharyali.mypracticeapplication.domain.ProductRepository
import javax.inject.Inject

class ProducrRepositoryImpl @Inject constructor(private val apiService: ProductApiService): ProductRepository {

    override suspend fun fetchProducts(): Result<List<Product>> = try{
        val result = apiService.fetchroducts().products.map { dTO ->
        dTO.toDomain()}
        Result.success(result)
    }
    catch (e: Exception) {
        Result.failure(e)
    }
}