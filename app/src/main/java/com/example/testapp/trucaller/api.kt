package com.example.testapp.trucaller
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface api {

    @GET("WebServiceJsonForApps.aspx")
    fun getData(@Query("type") type: String?,@Query("Mobile")Mobile:String?): Call<phonedata>
}