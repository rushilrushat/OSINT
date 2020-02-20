package com.example.testapp.trucaller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.testapp.R
import kotlinx.android.synthetic.main.activity_trucaller.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class trucaller : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trucaller)
        button.setOnClickListener {
            var phone = editText.text
            //val matcher: Matcher = Contacts.matcher(phone)
            if (phone!=null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://api.cdranalyst.in/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val service = retrofit.create(api::class.java)
                var a="TrueSearch"
                val call = service.getData(a.toString(), phone.toString())
                call!!.enqueue(object : Callback<phonedata> {
                    override fun onFailure(call: Call<phonedata>, t: Throwable) {
                        Log.d("Error", t.message)
                    }
                    override fun onResponse(call: Call<phonedata>, response: Response<phonedata>) {
                        Log.d("DATA", response.body().toString())
                        val users = arrayOf(
                            "Address= "+response.body()?.Address,
                            "Email= "+response.body()?.Email,
                            "Name= "+response.body()?.Name,
                            "S_no= "+response.body()?.SNo,
                            "ISP= "+response.body()?.MOperator,
                            "Image= "+response.body()?.Image,
                            "FaceBook= "+response.body()?.Facebook
                        )


                        val arraySize: Int = users.size
                        for (i in 0 until arraySize) {
                            textView5.append(users.get(i))
                            textView5.append("\n")
                        }
                    }
                })
            }
        }
    }
}
