package com.example.testapp.iplookup

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.R
import com.example.testapp.adupter
import com.example.testapp.createPDF
import com.example.testapp.iplookup.data.ipapi
import com.example.testapp.iplookup.data.ipdata
import com.itextpdf.text.Document
import com.itextpdf.text.Font
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.android.synthetic.main.activity_iplookup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class iplookup : AppCompatActivity() {
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private val IP_ADDRESS: Pattern = Pattern.compile(
        "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                + "|[1-9][0-9]|[0-9]))"
    )
    var key = arrayOf<String>(
        "HOST",
        "IP",
        "RDNS",
        "ASN",
        "ISP",
        "COUNTRY CODE",
        "REGION NAME",
        "REGION CODE",
        "CITY",
        "POSTAL CODE",
        "CONTINENTNAME",
        "CONTINENT CODE",
        "LATITUDE",
        "LONGITUDE",
        "METRO CODE",
        "TIMEZONE",
        "DATETIME"
    )
    var value: ArrayList<String> = ArrayList()

    var url1: String = "https://tools.keycdn.com/"

    private val STORAGE_CODE: Int = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iplookup)


        button2.setOnClickListener() {
            createPDF(applicationContext).createPdf(key,value)
        }
        button.setOnClickListener {

            var ip = editText.text
            var listView: ListView? = null
            var arrayAdapter: ArrayAdapter<String>? = null
            val matcher: Matcher = IP_ADDRESS.matcher(ip)
            if (matcher.matches()) {
                val retrofit1 = Retrofit.Builder()
                    .baseUrl(url1)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val service1 = retrofit1.create(Api::class.java)
                val call1 = service1.getData(ip.toString())
                call1!!.enqueue(object : Callback<ipdata> {
                    override fun onFailure(call: Call<ipdata>, t: Throwable) {
                        Log.d("Error", t.message)
                    }

                    override fun onResponse(call: Call<ipdata>, response: Response<ipdata>) {
                        Log.d("DATA", response.body().toString())

                        value.add(response.body()?.data?.geo?.host.toString())
                        value.add(response.body()?.data?.geo?.ip.toString())
                        value.add(response.body()?.data?.geo?.rdns.toString())
                        value.add(response.body()?.data?.geo?.asn.toString())
                        value.add(response.body()?.data?.geo?.isp.toString())
                        value.add(response.body()?.data?.geo?.continent_code.toString())
                        value.add(response.body()?.data?.geo?.region_name.toString())
                        value.add(response.body()?.data?.geo?.region_code.toString())
                        value.add(response.body()?.data?.geo?.city.toString())
                        value.add(response.body()?.data?.geo?.postal_code.toString())
                        value.add(response.body()?.data?.geo?.continent_name.toString())
                        value.add(response.body()?.data?.geo?.continent_code.toString())
                        value.add(response.body()?.data?.geo?.latitude.toString())
                        value.add(response.body()?.data?.geo?.longitude.toString())
                        value.add(response.body()?.data?.geo?.metro_code.toString())
                        value.add(response.body()?.data?.geo?.timezone.toString())
                        value.add(response.body()?.data?.geo?.datetime.toString())
                        button2.isVisible = true
                        layoutManager = LinearLayoutManager(applicationContext)
                        custom_view.layoutManager = layoutManager
                        custom_view.adapter = adupter( key, value, applicationContext)
                    }
                })



            } else {
                editText?.setError("Enter Proper ip")
                editText.isFocusable = true
            }
        }
    }




}
