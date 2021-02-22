package com.jbm.intactchallenge.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class Catalog @Inject constructor() {
    var productList = mutableListOf<Product>()
    var checkOutPrice = 0

    fun getProdctById(productId: Int): Product {
        return productList.find { it.id == productId }!!
    }

    fun updateCheckOutPrice() {
        checkOutPrice = 0

        for (p in productList) {
            if (p.wishListed == 1)
                checkOutPrice += p.price
        }
    }

    @Override
    override fun toString(): String {
        return "CheckOut Price = " + checkOutPrice + " - "+ productList.toString()
    }
}