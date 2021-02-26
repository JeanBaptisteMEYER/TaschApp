package com.jbm.intactchallenge.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jbm.intactchallenge.model.Catalog
import com.jbm.intactchallenge.model.MyRepository
import com.jbm.intactchallenge.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val myRepository: MyRepository): ViewModel() {

    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    var liveCatalog = MutableLiveData<Catalog>()
    var liveWishList = MutableLiveData(mutableListOf<Product>())
    var liveProduct = MutableLiveData<Product>()
    var liveTotalPrice = MutableLiveData(0)

    init {
        myRepository.getCatalogFromUrl { catalog -> //liveCatalog.value = catalog
                Log.d(TAG, "From ViewModel got catalog " + catalog.toString())
            liveCatalog.postValue(catalog)
        }
    }

    fun setProdctById(productId: Int) {
         liveProduct.value = liveCatalog.value!!.productList.find { it.id == productId }!!
    }

    fun addToWishList() {
        liveProduct.value?.wishListed = 1
        liveWishList.value?.add(liveProduct.value!!)
        liveTotalPrice.postValue(liveTotalPrice.value?.plus(liveProduct.value!!.price))
    }

    fun removeFromWishList() {
        liveProduct.value?.wishListed = 0
        liveWishList.value?.remove(liveProduct.value)
        liveTotalPrice.postValue(liveTotalPrice.value?.minus(liveProduct.value!!.price))
    }

    fun checkOut() {
        for (p in liveWishList.value!!)
            p.wishListed = 0

        liveWishList.postValue(mutableListOf())
        liveTotalPrice.value = 0
    }
}