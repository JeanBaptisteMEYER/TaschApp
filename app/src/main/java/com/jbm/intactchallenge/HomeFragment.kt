package com.jbm.intactchallenge

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.jbm.intactchallenge.adapter.CatalogAdapter
import com.jbm.intactchallenge.adapter.WishlistAdapter
import com.jbm.intactchallenge.databinding.FragmentHomeBinding
import com.jbm.intactchallenge.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment: Fragment() {
    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    val mainViewModel: MainViewModel by activityViewModels()

    // adapters for aur RecyclerViews
    val catalogAdapter: CatalogAdapter = CatalogAdapter()
    val wishlislAdapter: WishlistAdapter = WishlistAdapter()

    lateinit var binding: FragmentHomeBinding


    @Override
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        //Change Actionbar title to app name
        requireActivity().title = getString(R.string.app_name)

        // create and bind view to the catalog
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(context),
            container, false)

        // for total price update of the wishlist
        binding.viewModel = mainViewModel

        // The Recyclecler view that display the catalog
        binding.catalogRecyclerview.adapter = catalogAdapter

        // The Recyclecler view that display the wishlist
        binding.wishlistRecyclerview.adapter = wishlislAdapter

        // set on CheckOut clicklistener
        binding.setClickListener { view -> onCheckOutClick() }

        return binding.root
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe MainViewModel and update UI accordingly

        // Update the Catalog RecyclerView
        mainViewModel.liveCatalog.observe(viewLifecycleOwner, { catalog ->
            Log.d(TAG, "LiveCatalog from ViewModel changed " + catalog.toString())
            catalogAdapter.catalog = catalog
            catalogAdapter.notifyDataSetChanged()
        })

        // Update the wish list
        mainViewModel.liveWishList.observe(viewLifecycleOwner, { wishList ->
            Log.d(TAG, "WishList from ViewModel changed " + wishList.toString())
            wishlislAdapter.wishlist = wishList
            wishlislAdapter.notifyDataSetChanged()
        })

        // Update prices of wishlist
        mainViewModel.liveTotalPrice.observe(viewLifecycleOwner, { totalPrice ->
            Log.d(TAG, "TotalPrice from ViewModel changed " + totalPrice.toString())
            binding.invalidateAll()
        })
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

    fun doPositiveClick() {
        // clear the wishedlist and update ui
        mainViewModel.checkOut()
    }

    fun doNegativeClick() {
        //do nothing
    }
}