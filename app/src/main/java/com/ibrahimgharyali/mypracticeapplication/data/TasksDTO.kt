package com.ibrahimgharyali.mypracticeapplication.data

import kotlinx.serialization.Serializable

@Serializable
data class TasksDTO(
    val id: Int,
    val userId: Int,
    val title: String,
    val completed: Boolean
)