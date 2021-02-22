package com.jbm.intactchallenge.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class Catalog @Inject constructor() {
    var productList = mutableListOf<Product>()

    fun getProdctById(productId: Int): Product {
        return productList.find { it.id == productId }!!
    }

    fun getCheckOutPrice(): Int {
        var totPrice = 0

        for (p in productList) {
            if (p.wishListed == 1)
                totPrice += p.price
        }

        return totPrice
    }
}