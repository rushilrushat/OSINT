package com.osint.myapplication

data class phonedata(
    val `data`: Data
)
data class Data(
    val caller_name: CallerName,
    val cuid: String,
    val key: String,
    val md5_hash: String,
    val number_details: NumberDetails
)
data class CallerName(
    val caller_name: String,
    val error_code: Any
)
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
data class Formats(
    val E164: String,
    val international: String,
    val national: String
)