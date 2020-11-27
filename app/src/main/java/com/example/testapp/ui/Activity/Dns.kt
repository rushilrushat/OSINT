package com.example.testapp.ui.Activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testapp.R

class Dns : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dns)
        val pageUrl = "https://dnsdumpster.com"
        val intent = WebActivity.newIntent(this, pageUrl)
        startActivity(intent)
    }
}
