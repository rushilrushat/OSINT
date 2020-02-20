package com.example.testapp.iplookup

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.testapp.R
import com.example.testapp.iplookup.data.ipdata
import kotlinx.android.synthetic.main.activity_iplookup.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.regex.Matcher
import java.util.regex.Pattern

class iplookup : AppCompatActivity() {

    private val IP_ADDRESS: Pattern = Pattern.compile(
        "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                + "|[1-9][0-9]|[0-9]))"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button2.setOnClickListener() {
            createPdf("hii hello")
        }
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

                        button2.isVisible = true

                    }

                })
            } else {
                editText?.setError("Enter Proper ip")
                editText.isFocusable = true
            }
        }


    }

    private fun createPdf(sometext: String) { // create a new document
        val document = PdfDocument()
        // crate a page description
        var pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        // start a page
        var page = document.startPage(pageInfo)
        var canvas: Canvas = page.canvas
        var paint = Paint()
        paint.setColor(Color.RED)
        canvas.drawCircle(50F, 50F, 30F, paint)
        paint.setColor(Color.BLACK)
        canvas.drawText(sometext, 80F, 50F, paint)
        //canvas.drawt
// finish the page
        document.finishPage(page)
        // draw text on the graphics object of the page
// Create Page 2
        pageInfo = PdfDocument.PageInfo.Builder(300, 600, 2).create()
        page = document.startPage(pageInfo)
        canvas = page.canvas
        paint = Paint()
        paint.setColor(Color.BLUE)
        canvas.drawCircle(100F, 100F, 100F, paint)
        document.finishPage(page)
        // write the document content
        val directory_path: String =
            Environment.getExternalStorageDirectory().getPath().toString() + "/mypdf/"
        val file = File(directory_path)
        if (!file.exists()) {
            file.mkdirs()
        }
        val targetPdf = directory_path + "test-2.pdf"
        val filePath = File(targetPdf)
        try {
            document.writeTo(FileOutputStream(filePath))
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Log.e("main", "error " + e.toString())
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show()
        }
        // close the document
        document.close()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupPermissions() {
        val readexternalpermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val writeexternalpermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (readexternalpermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),123)
            Toast.makeText(applicationContext, "read Permission Denia", Toast.LENGTH_SHORT).show()
        }
        if (writeexternalpermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),123)
            Toast.makeText(applicationContext, "write Permission Denia", Toast.LENGTH_SHORT).show()
        }
    }

}
