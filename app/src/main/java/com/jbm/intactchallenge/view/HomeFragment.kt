package com.jbm.intactchallenge.view

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.bumptech.glide.Glide
import com.jbm.intactchallenge.MainActivity
import com.jbm.intactchallenge.R
import com.jbm.intactchallenge.model.MyRepository


class HomeFragment : Fragment(), MyRepository.View {

    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    lateinit var catalogLayout: LinearLayout
    lateinit var wishlistLayout: LinearLayout
    lateinit var subTotalTextView: TextView
    lateinit var totalTextView: TextView

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return initView(inflater.inflate(R.layout.home_fragment, container,false))
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

    fun initView(view: View): View {

        catalogLayout = view.findViewById(R.id.catalog_layout)
        wishlistLayout = view.findViewById(R.id.wishlist_layout)

        subTotalTextView = view.findViewById(R.id.sub_total_textview)
        totalTextView = view.findViewById(R.id.total_textview)

        view.findViewById<Button>(R.id.checkout_button).setOnClickListener{
            // show dialog to confirm
            AlertDialog.Builder(activity)
                .setTitle(R.string.alert_dialog_title)
                .setPositiveButton(
                    R.string.alert_dialog_ok,
                    DialogInterface.OnClickListener{ _, _ -> this.doPositiveClick() }
                )
                .setNegativeButton(
                    R.string.alert_dialog_cancel,
                    DialogInterface.OnClickListener { _, _ -> this.doNegativeClick() }
                ).show()
        }

        //Change Actionbar title to app name
        requireActivity().title = getString(R.string.app_name)

        return view
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
                .into(productView.findViewById(R.id.catalog_item_image));

            productView.setOnClickListener {
                showDetailFragment(product.id)
            }

            //add the new product view to our scrolling view
            catalogLayout.addView(productView)
        }
    }

    // this will update the Wishlist part of the UI.
    fun updateWishListUI () {

        var totalPrice = 0
        wishlistLayout.removeAllViews()

        for (product in (activity as MainActivity).myRepository.catalog) {
            if (product.wishListed == 1) {
                val wishedProductView: View =
                    layoutInflater.inflate(R.layout.wishlist_item, wishlistLayout, false)

                //add price to total price
                totalPrice = totalPrice + product.price

                ("$" + product.price.toString()).also { wishedProductView.findViewById<TextView>(R.id.wishlist_item_price).text = it }
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
                        layoutInflater.inflate(R.layout.color_view, wishlistLayout, false)

                    val background = resources.getDrawable(R.drawable.round_corner_color_shape)
                    background.setTint(android.graphics.Color.parseColor(color.code))
                    colorView.background = background

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
        (getString(R.string.total) + " " +  getString(R.string.currency) + totalPrice).also { totalTextView.text = it }
        (" " + getString(R.string.currency) + totalPrice).also { subTotalTextView.text = it }
    }

    fun showDetailFragment(productID: Int) {
        (activity as MainActivity).showDetailFragment(productID)
    }

    fun doPositiveClick() {
        // clear the wishedlist and update ui
        for (product in (activity as MainActivity).myRepository.catalog)
            product.wishListed = 0

        updateWishListUI()
    }

    fun doNegativeClick() {
        //do nothing
    }
}