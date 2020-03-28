package com.example.testapp.trucaller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.R
import com.example.testapp.ServiceBuilder
import com.example.testapp.adupter
import com.example.testapp.iplookup.Api
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
    private lateinit var layoutManager: RecyclerView.LayoutManager
    var value = ArrayList<String>()
    var key = arrayOf<String>(
            "Caller Name",
            "Valid",
            "National",
            "International",
            "Code",
            "Location",
            "Type",
            "Carrier",
            "Time Zone"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trucaller)
        button.setOnClickListener {
            var phone = "+91"+editText.text
            //val matcher: Matcher = Contacts.matcher(phone)
            if (phone!=null) {

                val messageService = ServiceBuilder("https://peoplestacks.com/").buildService(api::class.java)
                val requestCall = messageService.getData(phone.toString())
                requestCall!!.enqueue(object : Callback<phonedata> {
                    override fun onFailure(call: Call<phonedata>, t: Throwable) {
                        Log.d("Error++", t.message)
                    }
                    override fun onResponse(call: Call<phonedata>, response: Response<phonedata>) {
                        Log.d("DATA..", response.body().toString())

                        value.add(response.body()?.data?.caller_name?.caller_name.toString())
                        value.add(response.body()?.data?.number_details?.is_valid.toString())
                        value.add(response.body()?.data?.number_details?.formats?.national.toString())
                        value.add(response.body()?.data?.number_details?.formats?.international.toString())
                        value.add(response.body()?.data?.number_details?.calling_code.toString())
                        value.add(response.body()?.data?.number_details?.location.toString())
                        value.add(response.body()?.data?.number_details?.number_type.toString())
                        value.add(response.body()?.data?.number_details?.carrier.toString())
                        value.add(response.body()?.data?.number_details?.time_zone.toString())
                        button2.visibility = View.VISIBLE
                        layoutManager = LinearLayoutManager(applicationContext)
                        trucaller_recyclerview.layoutManager = layoutManager
                        trucaller_recyclerview.adapter = adupter(key, value, applicationContext)





                        button2.visibility= View.VISIBLE
                    }
                })
        }
    }
}
}
