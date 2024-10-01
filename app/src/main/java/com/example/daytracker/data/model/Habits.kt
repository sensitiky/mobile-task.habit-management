package com.example.daytracker.data.model

import java.sql.Timestamp

data class Habit(
    var id:Int = 0,
    var title:String = "",
    var description:String = "",
    var image:String = "",
    var createdAt:Timestamp,
    var updatedAt:Timestamp,
    var complete:Boolean,
)
