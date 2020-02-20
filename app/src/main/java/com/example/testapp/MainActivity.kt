package com.example.testapp

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.testapp.data.ipdata
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Matcher
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {
    private val IP_ADDRESS: Pattern = Pattern.compile(
        "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                + "|[1-9][0-9]|[0-9]))"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        button.setOnClickListener {
            var ip = editText.text
            var listView: ListView? = null
            var arrayAdapter: ArrayAdapter<String>? = null
            val matcher: Matcher = IP_ADDRESS.matcher(ip)
            if (matcher.matches()) {


                val retrofit = Retrofit.Builder()
                    .baseUrl("https://tools.keycdn.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val service = retrofit.create(Api::class.java)
                val call = service.getData(ip.toString())
                call!!.enqueue(object : Callback<ipdata> {
                    override fun onFailure(call: Call<ipdata>, t: Throwable) {
                        Log.d("Error", t.message)
                    }

                    override fun onResponse(call: Call<ipdata>, response: Response<ipdata>) {
                        Log.d("DATA", response.body().toString())

                        val users = arrayOf(
                            "HOST= " + response.body()?.data?.geo?.host,
                            "IP= " + response.body()?.data?.geo?.ip,
                            "RDNS= " + response.body()?.data?.geo?.rdns,
                            "ASN= " + response.body()?.data?.geo?.asn,
                            "ISP= " + response.body()?.data?.geo?.isp,
                            "COUNTRY NAME= " + response.body()?.data?.geo?.country_name,
                            "COUNTRY CODE= " + response.body()?.data?.geo?.continent_code,
                            "REGION NAME= " + response.body()?.data?.geo?.region_name,
                            "REGION CODE= " + response.body()?.data?.geo?.region_code,
                            "CITY= " + response.body()?.data?.geo?.city,
                            "POSTAL CODE= " + response.body()?.data?.geo?.postal_code,
                            "CONTINENTNAME= " + response.body()?.data?.geo?.continent_name,
                            "CONTINENT CODE= " + response.body()?.data?.geo?.continent_code,
                            "LATITUDE= " + response.body()?.data?.geo?.latitude,
                            "LONGITUDE= " + response.body()?.data?.geo?.longitude,
                            "METRO CODE= " + response.body()?.data?.geo?.metro_code,
                            "TIMEZONE= " + response.body()?.data?.geo?.timezone,
                            "DATETIME= " + response.body()?.data?.geo?.datetime
                        )


                        listView = findViewById(R.id.listview)
                        arrayAdapter = ArrayAdapter(
                            applicationContext,
                            android.R.layout.simple_list_item_1,
                            users
                        )
                        listView?.adapter = arrayAdapter

                    }

                })
            } else {
                editText?.setError("Enter Proper ip")
                editText.isFocusable = true
            }
        }


    }


}
