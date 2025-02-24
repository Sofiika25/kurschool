package com.example.kuratorschool.Models
import com.google.firebase.database.PropertyName
data class Test(
    val id: String = "",
    val idLecture: String = "",
    val title: String = "",
    val questions: Map<String, Question> = emptyMap(),
    @get:PropertyName("isAccessible")
    @set:PropertyName("isAccessible")
    var isAccessible: Boolean = false
)
