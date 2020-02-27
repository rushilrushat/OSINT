package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.testapp.iplookup.data.ipdata
import com.example.testapp.iplookup.Api
import com.example.testapp.iplookup.iplookup
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Matcher
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ip_button.setOnClickListener() {
            intent = Intent(this, com.example.testapp.iplookup.iplookup::class.java)
            startActivity(intent)
        }
        true_button.setOnClickListener(){
            intent = Intent(this, com.example.testapp.trucaller.trucaller::class.java)
            startActivity(intent)
        }

    }
}
