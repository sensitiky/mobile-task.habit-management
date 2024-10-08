package com.example.daytracker.data.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class Habit(
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var image: String = "",
    @Contextual var createdAt: Timestamp,
    @Contextual var updatedAt: Timestamp,
    var complete: Boolean,
)
