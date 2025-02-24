package com.example.kuratorschool.Models

import com.google.firebase.database.PropertyName

data class Case(
    val id: String = "",
    val question: String = "",
    val title: String = "",
    val userAnswers: Map<String, UserAnswer> = emptyMap(),
    @get:PropertyName("isAccessible")
    @set:PropertyName("isAccessible")
    var isAccessible: Boolean = false
)
