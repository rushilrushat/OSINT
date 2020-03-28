package com.example.testapp.iplookup

import com.example.testapp.iplookup.data.ipapi
import com.example.testapp.iplookup.data.ipdata
import com.example.testapp.webtype
import com.example.testapp.webtypeItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface Api {
    @GET("geo.json")
    fun getData(@Query("host") ip: String?): Call<ipdata>

    @GET("json/{ip}")
    fun getDataip(@Path("ip") ip: String?): Call<ipapi>

    @GET("index.php")
    fun gettype(@Query("type") type: String?): Call<webtype>

    @GET
    fun getMessages(@Url anotherUrl: String): Call<Any>
}