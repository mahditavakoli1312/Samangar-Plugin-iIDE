package com.example.data.response


data class ErrorBodyResponse(
    val data: Any,
    val message: String,
    val status: String
)