package com.osint.myapplication

data class Data(
    val caller_name: CallerName,
    val cuid: String,
    val key: String,
    val md5_hash: String,
    val number_details: NumberDetails
)