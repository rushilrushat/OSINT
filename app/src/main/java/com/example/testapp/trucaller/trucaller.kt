package com.example.testapp.trucaller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.view.isVisible
import com.example.testapp.R
import com.osint.myapplication.api
import com.osint.myapplication.phonedata
import kotlinx.android.synthetic.main.activity_iplookup.*
import kotlinx.android.synthetic.main.activity_trucaller.*
import kotlinx.android.synthetic.main.activity_trucaller.button
import kotlinx.android.synthetic.main.activity_trucaller.button2
import kotlinx.android.synthetic.main.activity_trucaller.editText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class trucaller : AppCompatActivity() {
    var users = arrayOf<String>()
    var listView: ListView? = null
    var arrayAdapter: ArrayAdapter<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trucaller)
        button.setOnClickListener {
            var phone = "+91"+editText.text
            //val matcher: Matcher = Contacts.matcher(phone)
            if (phone!=null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://peoplestacks.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val service = retrofit.create(api::class.java)
                var a="TrueSearch"
                val call = service.getData(phone.toString())
                call!!.enqueue(object : Callback<phonedata> {
                    override fun onFailure(call: Call<phonedata>, t: Throwable) {
                        Log.d("Error++", t.message)
                    }
                    override fun onResponse(call: Call<phonedata>, response: Response<phonedata>) {
                        Log.d("DATA..", response.body().toString())
                        users = arrayOf(
                            "Caller Name= " + response.body()?.data?.caller_name?.caller_name,
                            "Valid= " + response.body()?.data?.number_details?.is_valid,
                            "National= " + response.body()?.data?.number_details?.formats?.national,
                            "International" + response.body()?.data?.number_details?.formats?.international,
                            "Code= " + response.body()?.data?.number_details?.calling_code,
                            "Location= " + response.body()?.data?.number_details?.location,
                            "Type=" + response.body()?.data?.number_details?.number_type,
                            "Carrier =" + response.body()?.data?.number_details?.carrier,
                            "Time Zone ="+ response.body()?.data?.number_details?.time_zone


                        )


                        listView = findViewById(R.id.listview)
                        arrayAdapter = ArrayAdapter(
                            applicationContext,
                            android.R.layout.simple_list_item_1,
                            users
                        )
                        listView?.adapter = arrayAdapter

                        button2.isVisible = true
                    }
                })
        }
    }
}
}
