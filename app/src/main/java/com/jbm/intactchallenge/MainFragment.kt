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
import com.jbm.intactchallenge.model.MyRepository
import com.jbm.intactchallenge.model.Product
import com.jbm.intactchallenge.model.Size
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import kotlin.math.roundToInt


class MainFragment : Fragment(), MyRepository.View {

    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    lateinit var mainView: View
    lateinit var catalogLayout: LinearLayout
    lateinit var wishlistLayout: LinearLayout
    lateinit var myRepository: MyRepository

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
            for (product in myRepository.catalog)
                product.wishListed = 0

            updateWishListUI()

        }

        myRepository = MyRepository(requireContext(), this)
        lifecycleScope.launch { myRepository.loadJsonfromUrl() }

        return mainView
    }

    override fun onStart() {
        super.onStart()
        updateCatalogUI()
        updateWishListUI()
    }

    override fun onCatalogUpdate() {
        updateWishListUI()
        updateCatalogUI()
    }

    fun updateCatalogUI() {

        catalogLayout.removeAllViews()

        for (i in 0..myRepository.catalog.size-1) {
            //Inflate the catalog Item layout in the catalog parent View
            val productView: View = layoutInflater.inflate(R.layout.catalog_item, catalogLayout, false)

            //Update item title
            productView.findViewById<TextView>(R.id.catalog_item_title).text = myRepository.catalog[i].title

            //load image into ImageView
            Glide
                .with(this)
                .load(myRepository.catalog[i].imageUrl)
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

        for (product in myRepository.catalog) {
            if (product.wishListed == 1) {
                val wishedProductView: View =
                    layoutInflater.inflate(R.layout.wishlist_item, wishlistLayout, false)

                //add price to total price
                totalPrice = totalPrice + product.price

                wishedProductView.findViewById<TextView>(R.id.wishlist_item_price).text =
                    "$" + product.price.toString()
                wishedProductView.findViewById<TextView>(R.id.wishlist_item_title).text =
                    product.title
                wishedProductView.findViewById<TextView>(R.id.wishlist_item_short_description).text =
                    product.shordDescription

                Glide
                    .with(this)
                    .load(product.imageUrl)
                    .centerCrop()
                    .override(180, 180)
                    .placeholder(ColorDrawable(android.graphics.Color.BLACK))
                    .into(wishedProductView.findViewById<ImageView>(R.id.wishlist_item_image));

                // add the colored square to the wishlist item
                val colorLayout =
                    wishedProductView.findViewById<LinearLayout>(R.id.wishlist_item_color_layout)

                for (color in product.colors) {
                    val colorView =
                        layoutInflater.inflate(R.layout.product_color_view, wishlistLayout, false)
                    colorView.setBackgroundColor(android.graphics.Color.parseColor(color.code))

                    colorLayout.addView(colorView)
                }

                //Out of stock
                val outOfStockView =
                    wishedProductView.findViewById<TextView>(R.id.wishlist_item_out_of_stock)

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
}