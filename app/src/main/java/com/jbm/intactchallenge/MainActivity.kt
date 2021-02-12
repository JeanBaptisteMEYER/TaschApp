package com.jbm.intactchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.jbm.intactchallenge.model.Constantes
import com.jbm.intactchallenge.model.MyRepository
import com.jbm.intactchallenge.view.DetailFragment
import com.jbm.intactchallenge.view.HomeFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var myRepository: MyRepository
    val mainFragment = HomeFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)



        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .commitNow()
        }

        // Instantiate our repo
        myRepository = MyRepository(this, mainFragment)
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