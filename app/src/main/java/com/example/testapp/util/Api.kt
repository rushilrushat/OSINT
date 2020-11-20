package com.example.testapp.util

import com.example.testapp.Model.cdranalyst
import com.example.testapp.Model.ipapi
import com.example.testapp.Model.ipdata
import com.osint.myapplication.phonedata
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface trucallerapi {

    @GET("api/v1/lookup/people/sprout/{id}")
    fun getData(@Path("id")id:String?): Call<phonedata>

    @GET("/WebServiceJsonForApps.aspx")
    fun getCallerInfo(@Query("type") type:String, @Query("Mobile") mobile:String): Call<cdranalyst>
}

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