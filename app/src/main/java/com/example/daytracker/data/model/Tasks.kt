package com.example.daytracker.data.model

import java.sql.Date

class Tasks(
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var date: String = "",
    var time: String = "",
    var completed: Boolean = false,
    var completedDate: Date? = null
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