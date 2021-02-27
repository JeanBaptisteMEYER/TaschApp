package com.jbm.intactchallenge.model

import android.util.Log
import com.jbm.intactchallenge.utils.Constants
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import kotlin.math.roundToInt

// Main Class
class MyRepository @Inject constructor() {
    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    fun getCatalogFromUrl(completion: (Catalog) -> Unit) {
        val request = Request.Builder()
            .url(Constants().CATALOG_URL)
            .build()


        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val newCatalog = parseCatalogResponse(JSONObject(response.body?.string()))

                Log.d(TAG, "New Catalog downloaded and parsed = " + response.toString())

                completion(newCatalog)
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "OKHTTP error. Couldn't get catalog from URL = " + e.toString())
                completion(Catalog())
            }
        })
    }

    fun gsonParcing(jsonString: String) {
        Log.d(TAG, "jSonString = " + jsonString)
    }

    fun parseCatalogResponse(response: JSONObject): Catalog {
        var newCatalog = Catalog()
        var jsonProducts = response.getJSONArray("products")

        //Parse Json
        for (i in 0..jsonProducts.length()-1) {
            val jsonProduct = jsonProducts.getJSONObject(i)

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

            newCatalog.productList.add(newProduct)
        }

        return newCatalog
    }
}