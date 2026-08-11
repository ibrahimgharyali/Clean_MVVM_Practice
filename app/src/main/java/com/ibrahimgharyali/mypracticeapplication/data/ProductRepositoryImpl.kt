package com.ibrahimgharyali.mypracticeapplication.data

import com.ibrahimgharyali.mypracticeapplication.di.SportmonksToken
import com.ibrahimgharyali.mypracticeapplication.domain.Product
import com.ibrahimgharyali.mypracticeapplication.domain.ProductRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApiService,
    @param:SportmonksToken private val token: String
): ProductRepository {

    override suspend fun fetchProducts(): Result<List<Product>> = try {
        val result  = api.fetchProducts(token).products.map {
                it.toDomain()
            }
        Result.success(result)
    } catch (e: CancellationException) {
        throw e
    }catch (e: Exception) {
        Result.failure(e)
    }
}