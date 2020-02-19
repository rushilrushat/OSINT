package com.example.testapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.testapp.data.ipdata
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Matcher
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {
    private val IP_ADDRESS: Pattern = Pattern.compile(
        "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                + "|[1-9][0-9]|[0-9]))"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    button.setOnClickListener{
             var ip=editText.text
             val matcher: Matcher = IP_ADDRESS.matcher(ip)
             if (matcher.matches()) {


                 val retrofit = Retrofit.Builder()
                     .baseUrl("https://tools.keycdn.com/")
                     .addConverterFactory(GsonConverterFactory.create())
                     .build()
                 val service=retrofit.create(Api::class.java)
                 val call=service.getData(ip.toString())
                 call!!.enqueue(object :Callback<ipdata>{
                     override fun onFailure(call: Call<ipdata>, t: Throwable) {
                         Log.d("Error",t.message)
                     }

                     override fun onResponse(call: Call<ipdata>, response: Response<ipdata>) {
                         Log.d("DATA",response.body().toString())
                         /*val stringBuilder="HOST="+response.body()?.data?.geo?.host+"\n"+
                                 "IP="+response.body()?.data?.geo?.ip+"\n"+
                                 "RDNS="+response.body()?.data?.geo?.rdns+"\n"+
                                 "ASN="+response.body()?.data?.geo?.asn+"\n"+
                                 "ISP="+response.body()?.data?.geo?.isp+"\n"+
                                 "COUNTRY NAME="+response.body()?.data?.geo?.country_name+"\n"+
                                 "COUNTRY CODE="+response.body()?.data?.geo?.continent_code+"\n"+
                                 "REGION NAME="+response.body()?.data?.geo?.region_name+"\n"+
                                 "REGION CODE="+response.body()?.data?.geo?.region_code+"\n"+
                                 "city="+response.body()?.data?.geo?.city+"\n"+
                                 "postal_code="+response.body()?.data?.geo?.postal_code+"\n"+
                                 "continent_name="+response.body()?.data?.geo?.continent_name+"\n"+
                                 "continent_code="+response.body()?.data?.geo?.continent_code+"\n"+
                                 "latitude="+response.body()?.data?.geo?.latitude+"\n"+
                                 "longitude="+response.body()?.data?.geo?.longitude+"\n"+
                                 "metro_code="+response.body()?.data?.geo?.metro_code+"\n"+
                                 "timezone="+response.body()?.data?.geo?.timezone+"\n"+
                                 "datetime="+response.body()?.data?.geo?.datetime+"\n"
                         textView3.text=stringBuilder*/

                         val users = arrayOf(
                             "HOST= "+response.body()?.data?.geo?.host,
                                     "IP= "+response.body()?.data?.geo?.ip,
                                     "RDNS= "+response.body()?.data?.geo?.rdns,
                                     "ASN= "+response.body()?.data?.geo?.asn,
                                     "ISP= "+response.body()?.data?.geo?.isp,
                                     "COUNTRY NAME= "+response.body()?.data?.geo?.country_name,
                                     "COUNTRY CODE= "+response.body()?.data?.geo?.continent_code,
                                     "REGION NAME= "+response.body()?.data?.geo?.region_name,
                                     "REGION CODE= "+response.body()?.data?.geo?.region_code,
                                     "CITY= "+response.body()?.data?.geo?.city,
                                     "POSTAL CODE= "+response.body()?.data?.geo?.postal_code,
                                     "CONTINENTNAME= "+response.body()?.data?.geo?.continent_name,
                                     "CONTINENT CODE= "+response.body()?.data?.geo?.continent_code,
                                     "LATITUDE= "+response.body()?.data?.geo?.latitude,
                                     "LONGITUDE= "+response.body()?.data?.geo?.longitude,
                                     "METRO CODE= "+response.body()?.data?.geo?.metro_code,
                                     "TIMEZONE= "+response.body()?.data?.geo?.timezone,
                                     "DATETIME= "+response.body()?.data?.geo?.datetime
                         )


                         val arraySize: Int = users.size
                         for (i in 0 until arraySize) {
                             textView3.append(users.get(i))
                             textView3.append("\n")
                         }

                     }

                 })
             }else{
                 editText?.setError("Enter Proper ip")
                 editText.isFocusable = true
             }
         }



    }


}
