package com.example.testapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ip_button.setOnClickListener {
            intent = Intent(this, com.example.testapp.iplookup.iplookup::class.java)
            startActivity(intent)
        }
        true_button.setOnClickListener{
            intent = Intent(this, com.example.testapp.trucaller.trucaller::class.java)
            startActivity(intent)
        }
        tower_info.setOnClickListener {
            intent = Intent(this,com.example.testapp.Towerinfo::class.java)
            startActivity(intent)
        }

    }
}
