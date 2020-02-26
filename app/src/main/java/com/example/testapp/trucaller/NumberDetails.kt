package com.osint.myapplication

data class NumberDetails(
    val calling_code: Int,
    val carrier: String,
    val country_code: String,
    val formats: Formats,
    val is_valid: Boolean,
    val location: String,
    val number: String,
    val number_type: String,
    val time_zone: List<String>
)