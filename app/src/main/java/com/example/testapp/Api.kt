package com.example.testapp

import android.text.Editable
import com.example.testapp.data.ipdata
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface Api {
    @GET("geo.json")
    fun getData(@Query("host") ip: String?): Call<ipdata>
}