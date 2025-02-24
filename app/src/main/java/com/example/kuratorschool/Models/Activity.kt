package com.example.kuratorschool.Models

data class Activity(
    val activityId: String,
    val curatorId: String,
    val date: String,
    val groupId: Int,
    val nameActivity: String,
    val participants: List<Participant>
)
