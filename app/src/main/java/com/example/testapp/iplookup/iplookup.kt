package com.example.testapp.iplookup


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.*
import kotlinx.android.synthetic.main.activity_iplookup.*
import kotlinx.android.synthetic.main.list_item.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
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
    var key1 = arrayOf<String>(
            "HOST",
            "IP",
            "RDNS",
            "ASN",
            "ISP",
            "country_name",
            "COUNTRY CODE",
            "REGION NAME",
            "REGION CODE",
            "CITY",
            "POSTAL CODE",
            "CONTINENT NAME",
            "CONTINENT CODE",
            "LATITUDE",
            "LONGITUDE",
            "METRO CODE",
            "TIMEZONE",
            "DATETIME"
    )
    var key: ArrayList<String>? = ArrayList()
    var value: ArrayList<String>? = ArrayList()
    var selectedItems: ArrayList<String> = ArrayList()
    var selectedI: ArrayList<String> = ArrayList()
    var records: ArrayList<Data> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iplookup)

        var adapter = ArrayAdapter(applicationContext, R.layout.list_item, R.id.txt_title, selectedI)
        checkable_list?.adapter = adapter
        checkable_list.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            txt_title.isFocusable = false
            (view as CheckedTextView).text = records[i].name
            if (view.isChecked) {
                view.isChecked = false
                selectedItems.remove(records[i].name)
            } else {
                view.isChecked = true

                selectedItems.add(records[i].name)
            }
            Toast.makeText(this, view.text, Toast.LENGTH_SHORT).show()

        }

        adapter?.notifyDataSetChanged()


        val messageService0 = ServiceBuilder.buildService(Api::class.java)
        val requestCall0 = messageService0.gettype("ipinfo")
        requestCall0!!.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("Error", t.message)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val inputStream: InputStream = response.body()!!.byteStream()
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val s = bufferedReader.readLine()
                bufferedReader.close()
                var jsonObject: JSONObject? = null
                try {

                    jsonObject = JSONObject(s)

                    var jsonArray = jsonObject.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        var d = Data()
                        var json_data = jsonArray.getJSONObject(i);
                        d.id = json_data.getInt("id")
                        d.type = json_data.getString("type")
                        d.name = json_data.getString("name")
                        selectedI.add(json_data.getString("name"))
                        d.url = json_data.getString("url")
                        records.add(d)
                        adapter?.notifyDataSetChanged()

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }

        })



        layoutManager = LinearLayoutManager(applicationContext)
        iplookup_recyclerview.layoutManager = layoutManager
        var adpt = adupter(key!!, value!!, applicationContext)
        iplookup_recyclerview.adapter = adpt
        button2.setOnClickListener()
        {
                        createPDF(applicationContext).createPdf("IP Loockup",key!!, value!!)
        }

        button.setOnClickListener {

            var ip = editText.text
            val matcher: Matcher = IP_ADDRESS.matcher(ip)
            if (matcher.matches()) {
                if (selectedItems.isEmpty()) {
                    txt_title.isFocusable = true
                    txt_title.setError("Select Any One")


                } else {
                    for (i in selectedItems) {
                        if (i.equals("keycdn.com")) {
                            value?.clear()
                            key?.clear()
                            Toast.makeText(applicationContext,"Select keycdn.com",Toast.LENGTH_LONG).show()
                            checkable_list.visibility = View.GONE
                            val messageService = ServiceBuilder.buildService(Api::class.java)
                            val requestCall = messageService.getMessages("https://tools.keycdn.com/geo.json?host=" + ip.toString())
                            requestCall!!.enqueue(object : Callback<ResponseBody> {
                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    Log.d("Error", t.message)
                                }

                                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                    Log.d("DATA", response.body().toString())

                                    val inputStream: InputStream = response.body()!!.byteStream()
                                    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                                    val s = bufferedReader.readLine()
                                    bufferedReader.close()
                                    var jsonObject: JSONObject? = null
                                    try {
                                        jsonObject = JSONObject(s)
                                        value?.add("")
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("host").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("ip").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("rdns").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("asn").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("isp").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("country_name").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("country_code").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("region_name").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("region_code").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("city").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("postal_code").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("continent_name").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("continent_code").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("latitude").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("longitude").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("metro_code").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("timezone").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("datetime").toString())
                                        button2.visibility = View.VISIBLE
                                        key?.add("keycdn.com")
                                        key?.addAll(key1)
                                        adpt.notifyDataSetChanged()

                                    } catch (e: JSONException) {
                                        e.printStackTrace()

                                    }
                                }
                            })


                        }else if (i.equals("ip-api.com")){
                            Toast.makeText(applicationContext,"Select ip-api.com",Toast.LENGTH_LONG).show()
                            value?.clear()
                            key?.clear()
                            checkable_list.visibility = View.GONE
                            val messageService1 = ServiceBuilder.buildService(Api::class.java)
                            val requestCall1 = messageService1.getMessages("http://ip-api.com/json/" + ip.toString())
                            requestCall1!!.enqueue(object : Callback<ResponseBody> {
                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    Log.d("Error", t.message)
                                }

                                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                    Log.d("DATA", response.body().toString())

                                    val inputStream: InputStream = response.body()!!.byteStream()
                                    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                                    val s = bufferedReader.readLine()
                                    bufferedReader.close()
                                    var jsonObject: JSONObject? = null
                                    try {
                                        jsonObject = JSONObject(s)
                                        value?.add("")
                                        value?.add(jsonObject!!.getString("query").toString())
                                        value?.add(jsonObject!!.getString("country").toString())
                                        value?.add(jsonObject!!.getString("countryCode").toString())
                                        value?.add(jsonObject!!.getString("region").toString())
                                        value?.add(jsonObject!!.getString("regionName").toString())
                                        value?.add(jsonObject!!.getString("city").toString())
                                        value?.add(jsonObject!!.getString("zip").toString())
                                        value?.add(jsonObject!!.getString("lat").toString())
                                        value?.add(jsonObject!!.getString("lon").toString())
                                        value?.add(jsonObject!!.getString("isp").toString())
                                        value?.add(jsonObject!!.getString("org").toString())
                                        value?.add(jsonObject!!.getString("as").toString())
                                        value?.add(jsonObject!!.getString("timezone").toString())

                                        key?.add("ip-api.com")
                                        key?.add("Query")
                                        key?.add("country")
                                        key?.add("countryCode")
                                        key?.add("region")
                                        key?.add("regionName")
                                        key?.add("city")
                                        key?.add("zip")
                                        key?.add("lat")
                                        key?.add("lon")
                                        key?.add("isp")
                                        key?.add("org")
                                        key?.add("as")
                                        key?.add("timezone")
                                        button2.visibility = View.VISIBLE



                                        adpt.notifyDataSetChanged()

                                    } catch (e: JSONException) {
                                        e.printStackTrace()

                                    }
                                }
                            })
                        }else{
                            Toast.makeText(applicationContext,"All Api",Toast.LENGTH_LONG).show()
                            checkable_list.visibility = View.GONE
                            val messageService = ServiceBuilder.buildService(Api::class.java)
                            val requestCall = messageService.getMessages("https://tools.keycdn.com/geo.json?host=" + ip.toString())
                            requestCall!!.enqueue(object : Callback<ResponseBody> {
                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    Log.d("Error", t.message)
                                }

                                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                    Log.d("DATA", response.body().toString())

                                    val inputStream: InputStream = response.body()!!.byteStream()
                                    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                                    val s = bufferedReader.readLine()
                                    bufferedReader.close()
                                    var jsonObject: JSONObject? = null
                                    try {
                                        jsonObject = JSONObject(s)
                                        value?.add("")
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("host").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("ip").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("rdns").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("asn").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("isp").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("country_name").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("country_code").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("region_name").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("region_code").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("city").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("postal_code").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("continent_name").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("continent_code").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("latitude").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("longitude").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("metro_code").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("timezone").toString())
                                        value?.add(jsonObject!!.getJSONObject("data").getJSONObject("geo").getString("datetime").toString())
                                        key?.add("keycdn.com")
                                        key?.addAll(key1)
                                        adpt.notifyDataSetChanged()

                                    } catch (e: JSONException) {
                                        e.printStackTrace()

                                    }
                                }
                            })

                            //----------------------------------------------------------------------------------------------------------------------
                            val messageService1 = ServiceBuilder.buildService(Api::class.java)
                            val requestCall1 = messageService1.getMessages("http://ip-api.com/json/" + ip.toString())
                            requestCall1!!.enqueue(object : Callback<ResponseBody> {
                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    Log.d("Error", t.message)
                                }

                                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                    Log.d("DATA", response.body().toString())

                                    val inputStream: InputStream = response.body()!!.byteStream()
                                    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                                    val s = bufferedReader.readLine()
                                    bufferedReader.close()
                                    var jsonObject: JSONObject? = null
                                    try {
                                        jsonObject = JSONObject(s)
                                        value?.add("")
                                        value?.add(jsonObject!!.getString("query").toString())
                                        value?.add(jsonObject!!.getString("country").toString())
                                        value?.add(jsonObject!!.getString("countryCode").toString())
                                        value?.add(jsonObject!!.getString("region").toString())
                                        value?.add(jsonObject!!.getString("regionName").toString())
                                        value?.add(jsonObject!!.getString("city").toString())
                                        value?.add(jsonObject!!.getString("zip").toString())
                                        value?.add(jsonObject!!.getString("lat").toString())
                                        value?.add(jsonObject!!.getString("lon").toString())
                                        value?.add(jsonObject!!.getString("isp").toString())
                                        value?.add(jsonObject!!.getString("org").toString())
                                        value?.add(jsonObject!!.getString("as").toString())
                                        value?.add(jsonObject!!.getString("timezone").toString())

                                        key?.add("ip-api.com")
                                        key?.add("Query")
                                        key?.add("country")
                                        key?.add("countryCode")
                                        key?.add("region")
                                        key?.add("regionName")
                                        key?.add("city")
                                        key?.add("zip")
                                        key?.add("lat")
                                        key?.add("lon")
                                        key?.add("isp")
                                        key?.add("org")
                                        key?.add("as")
                                        key?.add("timezone")




                                        adpt.notifyDataSetChanged()

                                    } catch (e: JSONException) {
                                        e.printStackTrace()

                                    }
                                }
                            })
                            button2.visibility = View.VISIBLE
                        }
                    }
                }

            } else {
                editText?.setError("Enter Proper ip")
                editText.isFocusable = true
            }
        }
    }

}
