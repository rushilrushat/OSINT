package com.osint.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface api {

    @GET("api/v1/lookup/people/sprout/{id}")
    fun getData(@Path("id")id:String?): Call<phonedata>
}