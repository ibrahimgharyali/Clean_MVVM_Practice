package com.ibrahimgharyali.mypracticeapplication.domain

interface ProductRepository {

    suspend fun getProducts(): Result<List<Product>>
}