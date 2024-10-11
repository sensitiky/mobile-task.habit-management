package com.example.daytracker.data.model

import com.example.daytracker.data.local.SqlDateSerializer
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
    @Serializable(with = SqlDateSerializer::class) var completedDate: Date? = null
) {
    fun copy(
        id: Int = this.id,
        title: String = this.title,
        description: String = this.description,
        date: String = this.date,
        time: String = this.time,
        completed: Boolean = this.completed,
        completedDate: Date? = this.completedDate
    ): Tasks {
        return Tasks(id, title, description, date, time, completed, completedDate)
    }
}