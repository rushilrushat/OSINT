package com.example.testapp.iplookup.data

data class Geo(
    val asn: Int,
    val city: String,
    val continent_code: String,
    val continent_name: String,
    val country_code: String,
    val country_name: String,
    val datetime: String,
    val host: String,
    val ip: String,
    val isp: String,
    val latitude: Double,
    val longitude: Double,
    val metro_code: Any,
    val postal_code: String,
    val rdns: String,
    val region_code: String,
    val region_name: String,
    val timezone: String
)