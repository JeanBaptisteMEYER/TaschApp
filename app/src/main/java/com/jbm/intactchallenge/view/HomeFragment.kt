package com.jbm.intactchallenge.view

import android.app.AlertDialog
import android.content.*
import android.graphics.drawable.ColorDrawable
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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jbm.intactchallenge.MainActivity
import com.jbm.intactchallenge.R
import com.jbm.intactchallenge.adapter.HomeCatalogAdapter
import com.jbm.intactchallenge.databinding.WishlistItemBinding
import com.jbm.intactchallenge.model.Catalog
import com.jbm.intactchallenge.utils.Constants
import com.jbm.intactchallenge.model.MyRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment: Fragment() {

    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    lateinit var wishlistLayout: LinearLayout
    lateinit var subTotalTextView: TextView
    lateinit var totalTextView: TextView

    lateinit var catalogRecyclerView: RecyclerView
    @Inject lateinit var homeCatalogAdapter: HomeCatalogAdapter

    @Inject lateinit var catalog: Catalog

    val mBraodcastReceiver = object : BroadcastReceiver() {
        @Override
        override fun onReceive(p0: Context?, p1: Intent?) {
            when(p1?.action) {
                Constants().BROADCAST_ID_CATALOG_UPDATE -> updateHomeUI()
            }
        }
    }

    @Override
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return initView(inflater.inflate(R.layout.home_fragment, container,false))
    }

    @Override
    override fun onResume() {
        super.onResume()
        //Register to the broadcast Receiver to get notification with our model
        requireActivity().registerReceiver(mBraodcastReceiver, IntentFilter(Constants().BROADCAST_ID_CATALOG_UPDATE))
    }

    @Override
    override fun onStart() {
        super.onStart()
        updateCatalogUI()
        updateWishListUI()
    }

    @Override
    override fun onPause() {
        super.onPause()

        //Unregister to broadcasts
        requireActivity().unregisterReceiver(mBraodcastReceiver)
    }

    // Interface function. Will update all views
    // Gets call when the Repo has downloaded and parsed the Json file into the catalog
    fun updateHomeUI() {
        Log.d(TAG, "Home UI Update")
        updateWishListUI()
        updateCatalogUI()
    }

    //Initialize all views that are not dependent on the catalog
    fun initView(view: View): View {

        catalogRecyclerView = view.findViewById(R.id.catalog_recyclerview)
        catalogRecyclerView.adapter = homeCatalogAdapter

        wishlistLayout = view.findViewById(R.id.wishlist_layout)

        subTotalTextView = view.findViewById(R.id.sub_total_textview)
        totalTextView = view.findViewById(R.id.total_textview)

        //Change Actionbar title to app name
        requireActivity().title = getString(R.string.app_name)

        return view
    }

    fun updateCatalogUI() {
        homeCatalogAdapter.notifyDataSetChanged()
    }

    // this will update the Wishlist part of the UI.
    fun updateWishListUI () {

        var totalPrice = 0
        wishlistLayout.removeAllViews()

        Log.d(TAG, "repo = " + catalog.productList.toString())

        for (product in catalog.productList) {
            if (product.wishListed == 1) {

                val binding = WishlistItemBinding.inflate(LayoutInflater.from(context), wishlistLayout, false)
                binding.product = product

                //add price to total price
                totalPrice = totalPrice + product.price

                Glide
                    .with(this)
                    .load(product.imageUrl)
                    .centerCrop()
                    .override(180, 180)
                    .placeholder(ColorDrawable(android.graphics.Color.BLACK))
                    .into(binding.root.findViewById(R.id.wishlist_item_image));

                binding.root.setOnClickListener {
                    showDetailFragment(product.id)
                }

                //add the new product view to our scrolling view
                wishlistLayout.addView(binding.root)
            }
        }

        //update total price in the UI
        (getString(R.string.total) + " " +  getString(R.string.currency) + totalPrice).also { totalTextView.text = it }
        (" " + getString(R.string.currency) + totalPrice).also { subTotalTextView.text = it }
    }

    // called from activity when the onProceedToCheckOut button is clicked
    fun onCheckOutClick() {
        AlertDialog.Builder(activity)
            .setTitle(R.string.alert_dialog_title)
            .setPositiveButton(
                R.string.alert_dialog_ok,
                { _, _ -> doPositiveClick() }
            )
            .setNegativeButton(
                R.string.alert_dialog_cancel,
                { _, _ -> doNegativeClick() }
            ).show()
    }

    fun showDetailFragment(productID: Int) {
        (activity as MainActivity).showDetailFragment(productID)
    }

    fun doPositiveClick() {
        // clear the wishedlist and update ui
        for (product in catalog.productList)
            product.wishListed = 0

        updateWishListUI()
    }

    fun doNegativeClick() {
        //do nothing
    }
}