package com.ibrahimgharyali.mypracticeapplication.data

import com.ibrahimgharyali.mypracticeapplication.domain.Tasks
import kotlinx.serialization.Serializable

@Serializable
data class TasksDTO(
    val id: Int,
    val userId: Int,
    val title: String,
    val completed: Boolean
) {
    fun toDomain(): Tasks {
        return Tasks(
            id = this.id,
            title = this.title,
            completed = this.completed
        )
    }
}