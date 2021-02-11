package com.jbm.intactchallenge

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.jbm.intactchallenge.model.Color
import com.jbm.intactchallenge.model.Product
import com.jbm.intactchallenge.model.Size
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

class MainFragment : Fragment() {

    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    val url_json = "https://drive.google.com/uc?export=download&id=180NdUCDsmJgCSAfwaJIoWOVSVdvqyNu2"
    var catalog = mutableListOf<Product>()

    lateinit var catalogLayout: LinearLayout
    lateinit var wishlistLayout: LinearLayout

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container,false)

        catalogLayout = view.findViewById(R.id.catalog_layout)
        wishlistLayout = view.findViewById(R.id.wishlist_layout)

        return view
    }

    override fun onResume() {
        super.onResume()

        //start Coroutine that will load and parse Json from URL, then update UI
        loadJsonfromUrl(url_json)
    }

    fun updateUI() {
        for (i in 0..catalog.size-1) {
            //Inflate the catalog Item layout in the catalog parent View
            val catalogItem: View = layoutInflater.inflate(R.layout.catalog_item, catalogLayout, false)

            //Update item title
            catalogItem.findViewById<TextView>(R.id.catalog_item_title).text = catalog[i].title

            // update item image from URL
            /*
            Picasso.get()
                .load(catalog[i].imageUrl)
                .resize(200, 200)
                .centerCrop()
                .into(catalogItem.findViewById<ImageView>(R.id.catalog_item_image))

             */

            Glide.with(this)
                .load(catalog[i].imageUrl)
                .centerCrop()
                .into(catalogItem.findViewById<ImageView>(R.id.catalog_item_image));


            catalogLayout.addView(catalogItem)
        }
    }

    fun loadJsonfromUrl (url: String) {
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                Log.d(TAG, response.toString())
                parseCatalogResponse(response)
                updateUI()
                              },
            { error ->
                Log.d(TAG, "Error receiving Json response from Volley :$error")
                // if error then retrive JSON from local file
                parseCatalogResponse(getCatalogFromRaw())
                updateUI()
            })

        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest)
    }

    // this fun get the data from the JSON file in res/raw, parse it and build the list of game
    fun getCatalogFromRaw(): JSONObject {

        val rawData = resources.openRawResource(R.raw.catalogue).bufferedReader().use { it.readText() }
        return JSONObject(rawData)

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

        Log.d(TAG, "JSON parsed")
    }


}