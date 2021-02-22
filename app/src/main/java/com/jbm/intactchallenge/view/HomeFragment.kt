package com.jbm.intactchallenge.view

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jbm.intactchallenge.MainActivity
import com.jbm.intactchallenge.R
import com.jbm.intactchallenge.adapter.HomeCatalogAdapter
import com.jbm.intactchallenge.databinding.HomeFragmentBinding
import com.jbm.intactchallenge.databinding.WishlistItemBinding
import com.jbm.intactchallenge.model.Catalog
import com.jbm.intactchallenge.model.MyRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment: Fragment() {

    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    lateinit var wishlistLayout: LinearLayout

    lateinit var catalogRecyclerView: RecyclerView
    @Inject lateinit var homeCatalogAdapter: HomeCatalogAdapter

    @Inject lateinit var catalog: Catalog
    @Inject lateinit var myRepository: MyRepository

    lateinit var binding: HomeFragmentBinding

    @Override
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        return bindView(inflater, container)
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //observe our live Catalog and update UI when its data change
        myRepository.liveCatalog.observe(viewLifecycleOwner, Observer<Catalog> {
            catalog -> Log.d(TAG, "Catalog Live data changed " + catalog.toString())
            updateHomeUI()
        })
    }

    @Override
    override fun onStart() {
        super.onStart()
        updateHomeUI()
    }

    fun bindView(inflater: LayoutInflater, container: ViewGroup?): View {
        //Change Actionbar title to app name
        requireActivity().title = getString(R.string.app_name)

        // create and bind view to the catalog
        binding = HomeFragmentBinding.inflate(LayoutInflater.from(context),
            container, false)

        binding.catalog = catalog

        // The Recyclecler view that display the catalog
        catalogRecyclerView = binding.root.findViewById(R.id.catalog_recyclerview)
        catalogRecyclerView.adapter = homeCatalogAdapter

        wishlistLayout = binding.root.findViewById(R.id.wishlist_layout)

        return binding.root
    }

    // Updage all UI
    fun updateHomeUI() {
        Log.d(TAG, "Home UI Update")

        // catalog recycler view update
        homeCatalogAdapter.notifyDataSetChanged()

        // refresh Home UI - for Wishlist total price
        binding.invalidateAll()

        // refrech wishList section
        updateWishListUI()
    }


    // this will update the Wishlist part of the UI.
    fun updateWishListUI () {
        var totalPrice = 0
        wishlistLayout.removeAllViews()


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
        myRepository.checkOut()

    }

    fun doNegativeClick() {
        //do nothing
    }
}