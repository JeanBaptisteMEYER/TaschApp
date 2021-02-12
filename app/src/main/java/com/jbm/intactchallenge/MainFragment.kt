package com.jbm.intactchallenge

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.jbm.intactchallenge.model.MyRepository
import kotlinx.coroutines.launch


class MainFragment : Fragment(), MyRepository.View {

    val TAG: String =  "tag.jbm." + this::class.java.simpleName

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
            for (product in (activity as MainActivity).myRepository.catalog)
                product.wishListed = 0

            updateWishListUI()
        }

        (activity as MainActivity).myRepository = MyRepository(requireContext(), this)
        lifecycleScope.launch { (activity as MainActivity).myRepository.loadJsonfromUrl() }

        requireActivity().title = getString(R.string.app_name)

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

        for (product in (activity as MainActivity).myRepository.catalog) {
            //Inflate the catalog Item layout in the catalog parent View
            val productView: View = layoutInflater.inflate(R.layout.catalog_item, catalogLayout, false)

            //Update item title
            productView.findViewById<TextView>(R.id.catalog_item_title).text = product.title

            //load image into ImageView
            Glide
                .with(this)
                .load(product.imageUrl)
                .centerCrop()
                .override(200, 200)
                .into(productView.findViewById<ImageView>(R.id.catalog_item_image));

            productView.setOnClickListener {
                showDetailFragment(product.id)
            }

            //add the new product view to our scrolling view
            catalogLayout.addView(productView)
        }
    }

    fun updateWishListUI () {

        var totalPrice = 0
        wishlistLayout.removeAllViews()

        for (product in (activity as MainActivity).myRepository.catalog) {
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

                wishedProductView.setOnClickListener {
                    showDetailFragment(product.id)
                }

                //add the new product view to our scrolling view
                wishlistLayout.addView(wishedProductView)
            }
        }

        //update total price in the UI
        mainView.findViewById<TextView>(R.id.total_textview).text = getString(R.string.total) +
                " $" + totalPrice
        mainView.findViewById<TextView>(R.id.sub_total_textview).text = getString(R.string.sub_total) +
                " $" + totalPrice
    }

    fun showDetailFragment(productID: Int) {
        (activity as MainActivity).showDetailFragment(productID)
    }
}