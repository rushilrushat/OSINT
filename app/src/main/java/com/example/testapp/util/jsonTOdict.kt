package com.example.testapp.util

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

class jsonTOdict {
    val jsonMap = mutableMapOf<String, String>()
    var keys: ArrayList<String> = ArrayList()
    var values: ArrayList<String> = ArrayList()
    var count = 0
    fun Convetor(jsonObject: JSONObject): Triple<ArrayList<String>?, ArrayList<String>?, Int> {
        Log.d("json", "${jsonObject}")
        if (jsonObject is JSONObject) {
            val iter: Iterator<String> = jsonObject.keys()
            while (iter.hasNext()) {
                val key = iter.next()
                if (jsonObject.get(key) is JSONArray) {
                    var arr = jsonObject.getJSONArray(key)
                    for (i in 0 until arr.length()) {
                        var a = arr.getJSONObject(i)
                        var iter2: Iterator<String> = a.keys()
                        while (iter2.hasNext()) {
                            var key2 = iter2.next()
                            keys?.add(key2)
                            values?.add(a.get(key2).toString())
                            count++
                            jsonMap.put(key2, a.get(key2).toString())

                        }
                    }
                } else {

                    if (jsonObject.get(key) is Int) {
                        keys?.add(key)
                        values?.add(jsonObject.getInt(key).toString())
                        count++
                        jsonMap.put(key, jsonObject.getInt(key).toString())
                    } else if (jsonObject.get(key) is String) {
                        keys?.add(key)
                        values?.add(jsonObject.getInt(key).toString())
                        count++
                        jsonMap.put(key, jsonObject.get(key).toString())
                    } else if (jsonObject.get(key) is Double) {
                        keys?.add(key)
                        values?.add(jsonObject.getInt(key).toString())
                        count++
                        jsonMap.put(key, jsonObject.getDouble(key).toString())
                    } else if (jsonObject.isNull(key)) {
                        continue
                    } else {
                        Convetor(jsonObject.get(key) as JSONObject)
                    }
                }
            }
        }

        Log.d("json", "key=>${keys} value=>${values}")
        return Triple(keys, values, count)
    }
}