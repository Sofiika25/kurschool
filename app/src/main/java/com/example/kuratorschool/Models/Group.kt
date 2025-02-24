package com.example.kuratorschool.Models

data class Group(
    val curator_id: String = "",
    val group_id: Int = 0,
    val students: List<Student> = emptyList()
)
