package com.jbm.intactchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.jbm.intactchallenge.model.Constantes
import com.jbm.intactchallenge.model.MyRepository
import com.jbm.intactchallenge.view.DetailFragment
import com.jbm.intactchallenge.view.MainFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var myRepository: MyRepository
    val mainFragment = MainFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Instantiate our repo
        myRepository = MyRepository(this, mainFragment)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .commitNow()
        }
    }

    override fun onStart() {
        super.onStart()

        //Ask repo to Get catalog data from the web or local files
        lifecycleScope.launch { myRepository.loadJsonfromUrl() }
    }

    fun showDetailFragment(productId: Int) {
        val bundle = bundleOf(Constantes().ID_PARAM to productId)

        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container, DetailFragment.newInstance(productId))
            .commit()
    }
}