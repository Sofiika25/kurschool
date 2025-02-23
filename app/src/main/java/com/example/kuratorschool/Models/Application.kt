package com.example.kuratorschool.Models

data class Application(
    var fullName: String? = null,
    var groupNumber: String? = null,
    var phone: String? = null,
    var vkLink: String? = null,
    var telegram: String? = null,
    var curatorReason: String? = null,
    var positiveQualities: String? = null,
    var negativeQualities: String? = null,
    var studentExperience: String? = null,
    var status: String? = "На рассмотрении",
    val submissionDate: String = "",
    val user_id: String?=""
)
