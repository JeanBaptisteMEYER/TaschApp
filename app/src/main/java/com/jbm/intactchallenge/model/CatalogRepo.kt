package com.jbm.intactchallenge.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject


class CatalogRepo: ViewModel() {

    val TAG: String =  "tag.jbm." + this::class.java.simpleName
    var catalog = MutableLiveData<MutableList<Product>>()


    fun loadJsonfromUrl (c: Context, url: String) {
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.d(TAG, response.toString())
                parseCatalogResponse(response) },
            Response.ErrorListener { error ->
                Log.d(TAG, "Error receiving Json response from Volley :" + error.toString()) })

        Volley.newRequestQueue(c).add(jsonObjectRequest)
    }

    fun parseCatalogResponse(response: JSONObject) {

        // clear catalog before to populate it with product from Json
        synchronized(catalog){
            catalog.value?.clear()
        }

        var products = response.getJSONArray("products")

        //Parse Json
        for (i in 0..products.length()-1) {
            val jsonProduct = products.getJSONObject(i)

            var jsonProductColors = JSONArray()
            if (!jsonProduct.isNull("colors"))
                jsonProductColors = jsonProduct.getJSONArray("colors")

            val jsonProductSize = jsonProduct.getJSONObject("size")
            var newProduct = Product()

            //Parse standard product info
            newProduct.id = jsonProduct.getInt("id")
            newProduct.title = jsonProduct.getString("title")
            newProduct.brand = jsonProduct.getString("brand")
            newProduct.shordDescription = jsonProduct.getString("short_description")
            newProduct.fullDescription = jsonProduct.getString("description")
            newProduct.price = jsonProduct.getDouble("price")
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


            catalog.value?.add(newProduct)
        }
    }

}