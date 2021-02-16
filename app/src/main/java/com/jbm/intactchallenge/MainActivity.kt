package com.jbm.intactchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.jbm.intactchallenge.adapter.HomeCatalogAdapter
import com.jbm.intactchallenge.utils.Constants
import com.jbm.intactchallenge.model.MyRepository
import com.jbm.intactchallenge.view.DetailFragment
import com.jbm.intactchallenge.view.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    // Repo for access to Models
    @Inject lateinit var myRepository: MyRepository

    //@Inject lateinit var homeFragment: HomeFragment
    var homeFragment: HomeFragment = HomeFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, homeFragment)
                    .commitNow()
        }

        //LoadJson with a coroutine
        lifecycleScope.launch {
            myRepository.loadJsonfromUrl()
        }
    }

    // called when an item from the catalog list is clicked
    fun itemCatalogOnClick(view: View) {
        showDetailFragment(
                (homeFragment.catalogRecyclerView.getChildViewHolder(view) as HomeCatalogAdapter.HomeViewHolder).itemID)
    }

    fun showDetailFragment(productId: Int) {
        val bundle = bundleOf(Constants().ID_PARAM to productId)

        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container, DetailFragment.newInstance(productId))
            .commit()
    }
}