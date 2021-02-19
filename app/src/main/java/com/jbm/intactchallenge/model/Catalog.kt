package com.jbm.intactchallenge.model

import javax.inject.Inject

class Catalog @Inject constructor() {
    var productList = mutableListOf<Product>()

    fun getProdctById(productId: Int): Product {
        return productList.find { it.id == productId }!!
    }
}