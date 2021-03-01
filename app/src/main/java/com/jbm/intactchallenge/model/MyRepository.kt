package com.jbm.intactchallenge.model

import android.util.Log
import com.google.gson.Gson
import com.jbm.intactchallenge.utils.Constants
import okhttp3.*
import java.io.IOException
import javax.inject.Inject

// Repository class to access data
class MyRepository @Inject constructor() {
    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    fun getCatalogFromUrl(completion: (Catalog) -> Unit) {
        val request = Request.Builder()
            .url(Constants().CATALOG_URL)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {

                val responseBody = response.body?.string()
                val catalog = Gson().fromJson(responseBody, Catalog::class.java)

                Log.d(TAG, "New Catalog downloaded and parsed = " + catalog.toString())

                completion(catalog)
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "OKHTTP error. Couldn't get catalog from URL = " + e.toString())

                completion(Catalog())
            }
        })
    }
}