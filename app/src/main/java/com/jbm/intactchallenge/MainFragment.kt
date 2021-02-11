package com.jbm.intactchallenge

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jbm.intactchallenge.model.CatalogRepo
import com.jbm.intactchallenge.model.Color
import com.jbm.intactchallenge.model.Product
import com.jbm.intactchallenge.model.Size
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class MainFragment : Fragment() {

    val TAG: String =  "tag.jbm." + this::class.java.simpleName
    var catalog: MutableList<Product> = mutableListOf()


    val url_json = "https://drive.google.com/uc?export=download&id=180NdUCDsmJgCSAfwaJIoWOVSVdvqyNu2"
    lateinit var tv: TextView

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container,false)

        tv = view.findViewById(R.id.message)

        loadJsonfromUrl(url_json)

        return view
    }

    fun loadJsonfromUrl (url: String) {
        lifecycleScope.launch {
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener { response ->
                        Log.d(TAG, response.toString())
                        tv.text = response.toString()

                        parseCatalogResponse(response)
                    }, Response.ErrorListener { error ->
                        Log.d(TAG, "Error receiving Json response from Volley :" + error.toString())
            })

            Volley.newRequestQueue(requireContext()).add(jsonObjectRequest)
        }
    }

    fun parseCatalogResponse(response: JSONObject) {

        // clear catalog before to populate it with product from Json
        synchronized(catalog){
            catalog.clear()
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


            catalog.add(newProduct)
        }
    }


}