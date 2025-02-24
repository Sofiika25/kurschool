package com.example.kuratorschool.Models

import com.google.firebase.database.PropertyName

data class Lecture(
    var id: String = "",
    var title: String = "",
    var content: String = "",

    @get:PropertyName("isAccessible")
    @set:PropertyName("isAccessible")
    var isAccessible: Boolean = false
)
