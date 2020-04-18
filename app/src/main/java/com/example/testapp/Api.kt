package com.example.testapp

import com.example.testapp.iplookup.data.ipapi
import com.example.testapp.iplookup.data.ipdata
import okhttp3.ResponseBody
import org.json.JSONObject
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
    fun gettype(@Query("type") type: String?): Call<ResponseBody>

    @GET
    fun getMessages(@Url anotherUrl: String): Call<ResponseBody>
}