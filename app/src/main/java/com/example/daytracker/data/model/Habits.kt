package com.example.daytracker.data.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Habit(
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var image: String = "",
    @Contextual var createdAt: Long,
    @Contextual var updatedAt: Long,
    var complete: Boolean,
)
