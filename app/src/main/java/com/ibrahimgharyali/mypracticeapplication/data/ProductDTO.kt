package com.ibrahimgharyali.mypracticeapplication.data

import com.ibrahimgharyali.mypracticeapplication.domain.Product
import kotlinx.serialization.Serializable

@Serializable
data class ProductsArrayDTO(val products: List<ProductDTO>)
@Serializable
data class ProductDTO(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double
) {
    fun toDomain() =
        Product(
            id = this.id,
            title = this.title,
            desc = this.description,
            price = this.price,
        )
}