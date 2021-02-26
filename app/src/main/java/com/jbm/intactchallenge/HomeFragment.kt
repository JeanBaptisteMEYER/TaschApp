package com.jbm.intactchallenge

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jbm.intactchallenge.adapter.HomeCatalogAdapter
import com.jbm.intactchallenge.databinding.FragmentHomeBinding
import com.jbm.intactchallenge.databinding.ListItemWishlistBinding
import com.jbm.intactchallenge.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment: Fragment() {

    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    lateinit var wishlistLayout: LinearLayout

    lateinit var catalogRecyclerView: RecyclerView
    lateinit var homeCatalogAdapter: HomeCatalogAdapter

    lateinit var binding: FragmentHomeBinding

    val mainViewModel: MainViewModel by activityViewModels()

    @Override
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        return bindView(inflater, container)
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Update the Catalog RecyclerView
        mainViewModel.liveCatalog.observe(viewLifecycleOwner, { catalog ->
            Log.d(TAG, "LiveCatalog from ViewMode changed " + catalog.toString())
            homeCatalogAdapter.catalog = catalog
            homeCatalogAdapter.notifyDataSetChanged()
        })

        // Update the wish list
        mainViewModel.liveWishList.observe(viewLifecycleOwner, { wishList ->
            Log.d(TAG, "WishList from ViewMode changed " + wishList.toString())
            updateWishListUI()
        })

        // Update prices of wishlist
        mainViewModel.liveTotalPrice.observe(viewLifecycleOwner, { totalPrice ->
            Log.d(TAG, "TotalPrice from ViewMode changed " + totalPrice.toString())

            binding.invalidateAll()
        })
    }

    fun bindView(inflater: LayoutInflater, container: ViewGroup?): View {
        //Change Actionbar title to app name
        requireActivity().title = getString(R.string.app_name)

        // create and bind view to the catalog
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(context),
            container, false)

        binding.viewModel = mainViewModel

        // The Recyclecler view that display the catalog
        catalogRecyclerView = binding.root.findViewById(R.id.catalog_recyclerview)
        homeCatalogAdapter = HomeCatalogAdapter(requireContext())
        catalogRecyclerView.adapter = homeCatalogAdapter

        wishlistLayout = binding.root.findViewById(R.id.wishlist_layout)

        return binding.root
    }


    // this will update the Wishlist part of the UI.
    fun updateWishListUI () {
        wishlistLayout.removeAllViews()

        for (product in mainViewModel.liveWishList.value!!) {
            val binding = ListItemWishlistBinding.inflate(LayoutInflater.from(context), wishlistLayout, false)
            binding.product = product

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
        mainViewModel.checkOut()
    }

    fun doNegativeClick() {
        //do nothing
    }
}