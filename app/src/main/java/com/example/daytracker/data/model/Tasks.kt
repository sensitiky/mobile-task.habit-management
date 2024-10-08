package com.example.daytracker.data.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.sql.Date

@Serializable
class Tasks(
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    private var date: String = "",
    private var time: String = "",
    var completed: Boolean = false,
    @Contextual var completedDate: Date? = null
) {
    fun copy(
        id: Int = this.id,
        title: String = this.title,
        description: String = this.description,
        date: String = this.date,
        time: String = this.time,
        completed: Boolean = this.completed
    ): Tasks {
        return Tasks(id, title, description, date, time, completed)
    }
}