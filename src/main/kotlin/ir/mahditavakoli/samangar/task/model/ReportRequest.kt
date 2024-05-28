package com.example.tasks.model.remote

data class ReportRequest(
    val date: String,
    val description: String,
    val project: String,
    val tag: String?,
    val start: String,
    val end: String,
    val issue: String? = null
)
