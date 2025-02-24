package com.example.kuratorschool.Models


data class Question(
    val question: String = "",
    val type: String = "",
    val correctAnswer: String? = null,
    val correctAnswers: List<String>? = null,
    val options: Map<String, String>? = null
)







