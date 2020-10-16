package com.example.currencies.common.fixer

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FixerJsonResponse(
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date:String,
    val rates: Map<String, Float>
)