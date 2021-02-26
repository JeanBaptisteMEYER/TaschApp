package com.jbm.intactchallenge

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.jbm.intactchallenge.adapter.CatalogAdapter
import com.jbm.intactchallenge.adapter.WishlistAdapter
import com.jbm.intactchallenge.databinding.FragmentHomeBinding
import com.jbm.intactchallenge.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment: Fragment() {
    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    lateinit var catalogRecyclerView: RecyclerView
    lateinit var homeCatalogAdapter: CatalogAdapter

    lateinit var wishlistRecyclerView: RecyclerView
    lateinit var wishlislAdapter: WishlistAdapter

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

        // Observe MainViewModel and update UI accordingly

        // Update the Catalog RecyclerView
        mainViewModel.liveCatalog.observe(viewLifecycleOwner, { catalog ->
            Log.d(TAG, "LiveCatalog from ViewMode changed " + catalog.toString())
            homeCatalogAdapter.catalog = catalog
            homeCatalogAdapter.notifyDataSetChanged()
        })

        // Update the wish list
        mainViewModel.liveWishList.observe(viewLifecycleOwner, { wishList ->
            Log.d(TAG, "WishList from ViewMode changed " + wishList.toString())
            wishlislAdapter.wishlist = wishList
            wishlislAdapter.notifyDataSetChanged()
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

        // for total price update of the wishlist
        binding.viewModel = mainViewModel

        // The Recyclecler view that display the catalog
        catalogRecyclerView = binding.root.findViewById<RecyclerView>(R.id.catalog_recyclerview)
        homeCatalogAdapter = CatalogAdapter(requireContext())
        catalogRecyclerView.adapter = homeCatalogAdapter

        // The Recyclecler view that display the wishlist
        wishlistRecyclerView = binding.root.findViewById<RecyclerView>(R.id.wishlist_recyclerview)
        wishlislAdapter = WishlistAdapter(requireContext())
        wishlistRecyclerView.adapter = wishlislAdapter

        return binding.root
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