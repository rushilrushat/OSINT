package com.example.testapp.ui.Activity

import adupter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.ListView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.Data
import com.example.testapp.R
import com.example.testapp.service.ServiceBuilder
import com.example.testapp.util.Api
import com.example.testapp.util.createPDF
import kotlinx.android.synthetic.main.activity_trucaller.*
import kotlinx.android.synthetic.main.activity_trucaller.button
import kotlinx.android.synthetic.main.activity_trucaller.button2
import kotlinx.android.synthetic.main.activity_trucaller.editText
import kotlinx.android.synthetic.main.list_item.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class trucaller : AppCompatActivity() {
    private lateinit var layoutManager: RecyclerView.LayoutManager
    var key1 = arrayOf<String>(
            "Caller Name",
            "Number",
            "Valid",
            "Calling Code",
            "Country Code",
            "Location",
            "Number Type",
            "carrier",
            "time_zone"
    )
    var key: ArrayList<String>? = ArrayList()
    var value: ArrayList<String>? = ArrayList()
    var selectedItems: ArrayList<String> = ArrayList()
    var selectedI: ArrayList<String> = ArrayList()
    var records: ArrayList<Data> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trucaller)
        var adapter = ArrayAdapter(applicationContext, R.layout.list_item, R.id.txt_title, selectedI)

        turcaller_list?.adapter = adapter
        turcaller_list.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
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
        val requestCall0 = messageService0.gettype("trucaller")
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
        trucaller_recyclerview.layoutManager = layoutManager
        var adpt = adupter(key!!, value!!, this.applicationContext)
        trucaller_recyclerview.adapter = adpt

        button2.setOnClickListener() {
            createPDF(applicationContext).createPdf("Phone Number Information", key!!, value!!)
        }
        button.setOnClickListener {
            var phone = "+91" + editText.text
            //val matcher: Matcher = Contacts.matcher(phone)
            if (phone != null) {
                if (selectedItems.isEmpty()) {
                    //txt_title.isFocusable = true
                    txt_title.setError("Select Any One")


                } else {
                    val messageService = ServiceBuilder.buildService(Api::class.java)
                    val requestCall = messageService.getMessages("https://peoplestacks.com/api/v1/lookup/people/sprout/" + phone)
                    requestCall!!.enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.d("Error", t.message)
                        }

                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            Log.d("DATA", response.body().toString())
                            value?.clear()
                            key?.clear()
                            turcaller_list.visibility = View.GONE
                            val inputStream: InputStream = response.body()!!.byteStream()
                            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                            val s = bufferedReader.readLine()
                            bufferedReader.close()
                            var jsonObject: JSONObject? = null
                            try {
                                jsonObject = JSONObject(s)
                                value?.add(jsonObject!!.getJSONObject("data").getJSONObject("caller_name").getString("caller_name").toString())
                                value?.add(jsonObject!!.getJSONObject("data").getJSONObject("number_details").getString("number").toString())
                                value?.add(jsonObject!!.getJSONObject("data").getJSONObject("number_details").getString("is_valid").toString())
                                value?.add(jsonObject!!.getJSONObject("data").getJSONObject("number_details").getString("calling_code").toString())
                                value?.add(jsonObject!!.getJSONObject("data").getJSONObject("number_details").getString("country_code").toString())
                                value?.add(jsonObject!!.getJSONObject("data").getJSONObject("number_details").getString("location").toString())
                                value?.add(jsonObject!!.getJSONObject("data").getJSONObject("number_details").getString("number_type").toString())
                                value?.add(jsonObject!!.getJSONObject("data").getJSONObject("number_details").getString("carrier").toString())
                                value?.add(jsonObject!!.getJSONObject("data").getJSONObject("number_details").getJSONArray("time_zone").getString(0).toString())
                                key?.addAll(key1)
                                button2.visibility = View.VISIBLE
                                adpt.notifyDataSetChanged()
                            } catch (e: JSONException) {
                                e.printStackTrace()

                            }
                        }
                    })
                }
            } else {
                editText?.setError("Enter Valid Number")
                editText.isFocusable = true
            }
        }
    }
}
