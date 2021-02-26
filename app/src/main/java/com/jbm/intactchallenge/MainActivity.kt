package com.jbm.intactchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jbm.intactchallenge.adapter.HomeCatalogAdapter
import com.jbm.intactchallenge.model.MyRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    // Repo for access to Models
    @Inject lateinit var myRepository: MyRepository

    //@Inject lateinit var homeFragment: HomeFragment
    var homeFragment: HomeFragment = HomeFragment()
    var detailFragment: DetailFragment = DetailFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, homeFragment)
                    .commitNow()
        }
    }

    // called from Home layout onCLick
    fun onCheckOutClick(view: View) {
        homeFragment.onCheckOutClick()
    }

    // called from Detail layout onCLick
    fun onWishListClick (view: View) {
        detailFragment.onWishListClick(view)
    }

    // called when an item from the catalog list is clicked
    fun itemCatalogOnClick(view: View) {
        showDetailFragment(
                (homeFragment.catalogRecyclerView.getChildViewHolder(view)
                        as HomeCatalogAdapter.HomeViewHolder).catalogItemBinding.product!!.id)
    }

    // Show detail fragment for the given product
    fun showDetailFragment(productId: Int) {
        detailFragment = DetailFragment.newInstance(productId)

        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container, detailFragment)
            .commit()
    }
}