package com.example.daytracker.data.model

class User(
    var id: Int = 0,
    var avatar: String = "",
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var habits: List<Habit> = listOf(),
    var tasks: List<Tasks> = listOf()
)