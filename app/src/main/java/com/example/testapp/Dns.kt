package com.example.testapp


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Dns : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dns)
        val pageUrl = "https://dnsdumpster.com"
        val intent = WebActivity.newIntent(this, pageUrl)
    }
}
