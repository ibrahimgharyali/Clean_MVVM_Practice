package com.ibrahimgharyali.mypracticeapplication.data

import kotlinx.serialization.Serializable

@Serializable
data class ProductObjectDTO (
    val products: List<ProductDTO>
)
@Serializable
data class ProductDTO (
    val id: Int,
    val title: String,
    val description: String,
    val price: Double
)