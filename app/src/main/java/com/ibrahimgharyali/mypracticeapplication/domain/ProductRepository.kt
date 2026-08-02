package com.ibrahimgharyali.mypracticeapplication.domain

interface ProductRepository {

    suspend fun fetchProducts(): Result<List<Product>>
}