package com.ibrahimgharyali.mypracticeapplication.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)