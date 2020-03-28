package com.example.testapp

class webtype : ArrayList<webtypeItem>()

data class webtypeItem(
    val id: String,
    val name: String,
    val type: String,
    val url: String
)