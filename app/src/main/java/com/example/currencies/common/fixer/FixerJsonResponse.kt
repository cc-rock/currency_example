package com.example.currencies.common.fixer

data class FixerJsonResponse(
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date:String,
    val rates: Map<String, Float>
)