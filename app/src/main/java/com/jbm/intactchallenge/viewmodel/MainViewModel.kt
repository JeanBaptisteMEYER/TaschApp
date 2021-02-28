package com.jbm.intactchallenge.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jbm.intactchallenge.model.Catalog
import com.jbm.intactchallenge.model.MyRepository
import com.jbm.intactchallenge.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class MainViewModel @Inject constructor(private val myRepository: MyRepository): ViewModel() {

    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    var liveCatalog = MutableLiveData<Catalog>()
    var liveWishList = MutableLiveData(mutableListOf<Product>())
    var liveProduct = MutableLiveData<Product>()
    var liveTotalPrice = MutableLiveData(0)

    init {
        // on first initialisation, get and parse the catalog from URL
        myRepository.getCatalogFromUrl { catalog ->
            Log.d(TAG, "From ViewModel got catalog " + catalog.toString())
            liveCatalog.postValue(catalog)
        }
    }

    fun setLiveProductById(productId: Int) {
        liveCatalog.value?.let {
            val newProduct = it.products.find { it.id == productId }
            liveProduct.value = newProduct
        }
    }

    fun addToWishList() {
        liveProduct.value?.let {
            it.wishListed = 1
            liveWishList.value?.add(it)
            val newPrice = liveTotalPrice.value?.plus(it.getRoundedPrice())
            liveTotalPrice.value = newPrice
        }
    }

    fun removeFromWishList() {
        liveProduct.value?.let {
            it.wishListed = 0
            liveWishList.value?.remove(it)
            val newPrice = liveTotalPrice.value?.minus(it.getRoundedPrice())
            liveTotalPrice.value = newPrice
        }
    }

    fun checkOut() {
        liveWishList.value?.let {
            for (p in it)
                p.wishListed = 0

            it.clear()
        }

        liveWishList.value = liveWishList.value
        liveTotalPrice.value = 0
    }
}