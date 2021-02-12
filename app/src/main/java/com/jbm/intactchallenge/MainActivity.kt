package com.jbm.intactchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import com.jbm.intactchallenge.model.Constantes
import com.jbm.intactchallenge.model.MyRepository
import com.jbm.intactchallenge.view.DetailFragment
import com.jbm.intactchallenge.view.MainFragment

class MainActivity : AppCompatActivity() {

    lateinit var myRepository: MyRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }

    fun showDetailFragment(productId: Int) {
        val bundle = bundleOf(Constantes().ID_PARAM to productId)

        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container, DetailFragment.newInstance(productId))
            .commit()
    }
}