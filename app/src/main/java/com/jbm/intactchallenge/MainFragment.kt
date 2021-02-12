package com.jbm.intactchallenge

import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayoutStates
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.jbm.intactchallenge.model.Color
import com.jbm.intactchallenge.model.Product
import com.jbm.intactchallenge.model.Size
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class MainFragment : Fragment() {

    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    val url_json = "https://drive.google.com/uc?export=download&id=180NdUCDsmJgCSAfwaJIoWOVSVdvqyNu2"
    var catalog = mutableListOf<Product>()

    lateinit var mainView: View
    lateinit var catalogLayout: LinearLayout
    lateinit var wishlistLayout: LinearLayout

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mainView = inflater.inflate(R.layout.main_fragment, container,false)

        catalogLayout = mainView.findViewById(R.id.catalog_layout)
        wishlistLayout = mainView.findViewById(R.id.wishlist_layout)
        mainView.findViewById<Button>(R.id.checkou_button).setOnClickListener{
            // clear the wishedlist and update ui
            for (product in catalog)
                product.wishListed = 0
            updateWishListUI()

        }

        return mainView
    }

    override fun onResume() {
        super.onResume()

        //start Coroutine that will load and parse Json from URL, then update UI
        lifecycleScope.launch { loadJsonfromUrl(url_json) }
    }

    fun updateCatalogUI() {
        for (i in 0..catalog.size-1) {
            //Inflate the catalog Item layout in the catalog parent View
            val productView: View = layoutInflater.inflate(R.layout.catalog_item, catalogLayout, false)

            //Update item title
            productView.findViewById<TextView>(R.id.catalog_item_title).text = catalog[i].title

            /*
            Picasso.get()
                .load(catalog[i].imageUrl)
                .resize(200, 200)
                .centerCrop()
                .into(productView.findViewById<ImageView>(R.id.catalog_item_image))

             */


            //load image into ImageView
            Glide
                .with(this)
                .load(catalog[i].imageUrl)
                .centerCrop()
                .override(200, 200)
                .into(productView.findViewById<ImageView>(R.id.catalog_item_image));


            //add the new product view to our scrolling view
            catalogLayout.addView(productView)
        }
    }

    fun updateWishListUI () {

        var totalPrice = 0
        wishlistLayout.removeAllViews()

        for (product in catalog) {
            if (product.wishListed == 1) {
                val wishedProductView: View = layoutInflater.inflate(R.layout.wishlist_item, wishlistLayout, false)

                //add price to total price
                totalPrice = totalPrice + product.price

                wishedProductView.findViewById<TextView>(R.id.wishlist_item_price).text = "$" + product.price.toString()
                wishedProductView.findViewById<TextView>(R.id.wishlist_item_title).text = product.title
                wishedProductView.findViewById<TextView>(R.id.wishlist_item_short_description).text = product.shordDescription

                /*
                Picasso.get()
                    .load(catalog[i].imageUrl)
                    .resize(200, 200)
                    .centerCrop()
                    .into(wishedProductView.findViewById<ImageView>(R.id.wishlist_item_image))

                 */

                Glide
                    .with(this)
                    .load(product.imageUrl)
                    .centerCrop()
                    .override(180, 180)
                    .placeholder(ColorDrawable(android.graphics.Color.BLACK))
                    .into(wishedProductView.findViewById<ImageView>(R.id.wishlist_item_image));

                // add the colored square to the wishlist item
                val colorLayout = wishedProductView.findViewById<LinearLayout>(R.id.wishlist_item_color_layout)

                for (color in product.colors) {
                    val colorView = layoutInflater.inflate(R.layout.product_color_view, wishlistLayout, false)
                    colorView.setBackgroundColor(android.graphics.Color.parseColor(color.code))

                    colorLayout.addView(colorView)
                }

                //Out of stock
                val outOfStockView = wishedProductView.findViewById<TextView>(R.id.wishlist_item_out_of_stock)

                if (product.quantityInStock == 0)
                    outOfStockView.visibility = View.VISIBLE
                else
                    outOfStockView.visibility = View.GONE


                //add the new product view to our scrolling view
                wishlistLayout.addView(wishedProductView)
            }
        }

        //update total price in the UI
        mainView.findViewById<TextView>(R.id.total_textview).text = getString(R.string.total) +
                " $" + totalPrice
        mainView.findViewById<TextView>(R.id.sub_total_textview).text = getString(R.string.total) +
                " $" + totalPrice
    }

    fun loadJsonfromUrl (url: String) {
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                Log.d(TAG, response.toString())
                parseCatalogResponse(response)
                updateCatalogUI()
                updateWishListUI()
                              },
            { error ->
                Log.d(TAG, "Error receiving Json response from Volley :$error")
                // if error then retrive JSON from local file
                parseCatalogResponse(getCatalogFromRaw())
                updateCatalogUI()
                updateWishListUI()
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


            catalog.add(newProduct)
        }

        Log.d(TAG, "JSON parsed")
    }


}