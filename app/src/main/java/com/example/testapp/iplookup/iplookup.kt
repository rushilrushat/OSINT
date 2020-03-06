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

    private val STORAGE_CODE: Int = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iplookup)


        button2.setOnClickListener() {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                //system OS >= Marshmallow(6.0), check permission is enabled or not
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    //permission was not granted, request it
                    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, STORAGE_CODE)
                } else {
                    //permission already granted, call savePdf() method
                    createPdf(key, value)
                }
            } else {
                //system OS < marshmallow, call savePdf() method
                createPdf(key, value)
            }
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


                        layoutManager = LinearLayoutManager(applicationContext)
                        custom_view.layoutManager = layoutManager
                        custom_view.adapter = adupter(key, value, applicationContext)



                        button2.isVisible = true

                    }

                })
            } else {
                editText?.setError("Enter Proper ip")
                editText.isFocusable = true
            }
        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted, call savePdf() method
                    //createPdf("hii hello")
                } else {
                    //permission from popup was denied, show error message
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createPdf(key: Array<String>, value: ArrayList<String>) {
        // create a new document
        val mDoc = Document()
        //pdf file name
        val mFileName = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(System.currentTimeMillis())
        //pdf file path
        val mFilePath =
            Environment.getExternalStorageDirectory().toString() + "/" + mFileName + ".pdf"
        try {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))

            //open the document for writing
            mDoc.open()

            //get text from EditText i.e. textEt
            var catFont = Font()
            //add author of the document (metadata)
            mDoc.addAuthor("Anonymous")
            mDoc.addTitle("IP Information")
            var pdftable = PdfPTable(2)

            var cell = PdfPCell(Phrase("Data Fatch From keycdn.com"))
            cell.setColspan(2)
            pdftable.addCell(cell)
            //add paragraph to the document
            //mDoc.add(Paragraph(sometext))
            for (j in key) {
                var phrase = Phrase(j);
                pdftable.addCell(PdfPCell(phrase));


            }
            for (i in value) {
                var phrase = Phrase(i);
                pdftable.addCell(PdfPCell(phrase));
                //mDoc.add(Paragraph(i))

            }
            pdftable.completeRow()
            mDoc.add(pdftable)
            //close document
            mDoc.close()

            //show file saved message with file name and path
            Toast.makeText(this, "$mFileName.pdf\nis saved to\n$mFilePath", Toast.LENGTH_SHORT)
                .show()


            Log.d("path ",mFilePath.toString())
            intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.fromFile(File(mFilePath)), "application/pdf")
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)

        } catch (e: Exception) {
            //if anything goes wrong causing exception, get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }


}
