package com.jbm.intactchallenge.model

import android.content.Context
import android.content.Intent
import android.util.Log
import com.jbm.intactchallenge.R
import com.jbm.intactchallenge.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import kotlin.math.roundToInt


// Main Class
class MyRepository @Inject constructor(
    @ApplicationContext val context: Context,
    val catalog: Catalog)
{
    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    val catalogURL = "https://drive.google.com/uc?export=download&id=180NdUCDsmJgCSAfwaJIoWOVSVdvqyNu2"

    fun loadJsonfromUrl () {
        val request = Request.Builder()
            .url(catalogURL)
            .build()
        

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, response.toString())

                //ParseJSON into a list of product than update UI
                MainScope().launch { parseCatalogResponse(JSONObject(response.body?.string())) }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, e.toString() + ". /// JSON will be loaded from raw folder")
                MainScope().launch { parseCatalogResponse(getCatalogFromRaw()) }
            }
        })
    }

    // this fun get the data from the JSON file in res/raw, parse it and build the list of game
    fun getCatalogFromRaw(): JSONObject {
        val rawData = context.resources.openRawResource(R.raw.catalogue).bufferedReader().use { it.readText() }
        return JSONObject(rawData)

    }

    fun parseCatalogResponse(response: JSONObject) {
        // clear catalog before to populate it with product from Json
        catalog.productList.clear()

        var products = response.getJSONArray("products")

        //Parse Json
        for (i in 0..products.length()-1) {
            val jsonProduct = products.getJSONObject(i)

            var jsonProductColors = JSONArray()
            if (!jsonProduct.isNull("colors"))
                jsonProductColors = jsonProduct.getJSONArray("colors")

            val jsonProductSize = jsonProduct.getJSONObject("size")


            //Parse standard product info
            var newProduct = Product(jsonProduct.getInt("id"))
            newProduct.title = jsonProduct.getString("title")
            newProduct.brand = jsonProduct.getString("brand")
            newProduct.shordDescription = jsonProduct.getString("short_description")
            newProduct.fullDescription = jsonProduct.getString("description")
            newProduct.price = jsonProduct.getDouble("price").roundToInt()
            newProduct.imageUrl = jsonProduct.getString("image")
            newProduct.quantityInStock = jsonProduct.getInt("quantity")

            // Parse product Colors
            for (i in 0..jsonProductColors.length()-1) {
                newProduct.colors.add(Color(jsonProductColors.getJSONObject(i).getString("code"),
                    jsonProductColors.getJSONObject(i).getString("name")))
            }

            // Parse product size
            newProduct.size = Size(jsonProductSize.getString("H"),
                jsonProductSize.getString("W"),
                jsonProductSize.getString("D"))

            catalog.productList.add(newProduct)
        }

        Log.d(TAG, "JSON parsed " + catalog.productList.toString())

        context.sendBroadcast(Intent().setAction(Constants().BROADCAST_ID_CATALOG_UPDATE))
    }
}