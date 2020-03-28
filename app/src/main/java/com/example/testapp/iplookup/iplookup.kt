package com.example.testapp.iplookup

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.*
import com.example.testapp.iplookup.data.ipapi
import com.example.testapp.iplookup.data.ipdata
import kotlinx.android.synthetic.main.activity_iplookup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Matcher
import java.util.regex.Pattern

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
    var value = ArrayList<String>()

lateinit var adpt:adupter
    var url1: String = "https://tools.keycdn.com/"
    var arrayAdapter: ArrayAdapter<String>? = null
    private val STORAGE_CODE: Int = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iplookup)

        layoutManager = LinearLayoutManager(applicationContext)
        iplookup_recyclerview.layoutManager = layoutManager
        adpt=adupter(key, value, applicationContext)
        iplookup_recyclerview.adapter = adpt


        arrayAdapter = ArrayAdapter(
                applicationContext,
                android.R.layout.simple_list_item_multiple_choice,
                key
        )
        listview?.adapter = arrayAdapter
        listview.setOnItemClickListener(OnItemClickListener { adapterView, view, index, l ->
            val clickItemObj = adapterView.adapter.getItem(index)
            Toast.makeText(this, "You clicked $clickItemObj", Toast.LENGTH_SHORT).show()
        })
        button2.setOnClickListener() {
            createPDF(applicationContext).createPdf(key, value)
        }
        button.setOnClickListener {
            listview.visibility = View.GONE
            var ip = editText.text
            var listView: ListView? = null
            var arrayAdapter: ArrayAdapter<String>? = null
            val matcher: Matcher = IP_ADDRESS.matcher(ip)
            if (matcher.matches()) {
                val messageService = ServiceBuilder("https://tools.keycdn.com/").buildService(Api::class.java)
                val requestCall = messageService.getData(ip.toString())
                requestCall.enqueue(object : Callback<ipdata> {
                    override fun onFailure(call: Call<ipdata>, t: Throwable) {
                        Log.d("Error", t.message)
                    }

                    override fun onResponse(call: Call<ipdata>, response: Response<ipdata>) {
                        Log.d("DATA",response.message().toString())

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
                        button2.visibility = View.VISIBLE
                        adpt.notifyDataSetChanged()


                        val messageService = ServiceBuilder("http://ip-api.com").buildService(Api::class.java)
                        val requestCall = messageService.getDataip(ip.toString())
                        requestCall!!.enqueue(object : Callback<ipapi> {
                            override fun onFailure(call: Call<ipapi>, t: Throwable) {
                                Log.d("ip-api Error", t.message)
                            }

                            override fun onResponse(call: Call<ipapi>, response: Response<ipapi>) {
                                Log.d("ip-api data", response.body().toString())
                                value.add(response.body()?.query.toString())
                                value.add(response.body()?.status.toString())
                                value.add(response.body()?.isp.toString())
                                value.add(response.body()?.country.toString())
                                value.add(response.body()?.countryCode.toString())
                                value.add(response.body()?.city.toString())
                                value.add(response.body()?.`as`.toString())
                                value.add(response.body()?.region.toString())
                                value.add(response.body()?.regionName.toString())
                                value.add(response.body()?.lon.toString())
                                value.add(response.body()?.lat.toString())
                                value.add(response.body()?.org.toString())
                                value.add(response.body()?.timezone.toString())
                                value.add(response.body()?.zip.toString())
                                adpt.notifyDataSetChanged()
                            }
                        })
                    }
                })


            } else {
                editText?.setError("Enter Proper ip")
                editText.isFocusable = true
            }
        }
    }


}




