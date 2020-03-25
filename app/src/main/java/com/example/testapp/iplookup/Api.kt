package com.example.testapp.iplookup

import com.example.testapp.iplookup.data.ipapi
import com.example.testapp.iplookup.data.ipdata
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface Api {
    @GET("geo.json")
    fun getData(@Query("host") ip: String?): Call<ipdata>

    @GET("json/{ip}")
    fun getDataip(@Path("ip") ip: String?): Call<ipapi>
}