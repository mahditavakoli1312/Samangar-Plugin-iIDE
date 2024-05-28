package com.example.data.network

internal class NetworkException(val serverMessage: String, val code: Int) : Exception()
