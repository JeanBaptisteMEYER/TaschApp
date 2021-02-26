package com.jbm.intactchallenge.model

class Catalog {
    var productList = mutableListOf<Product>()

    @Override
    override fun toString(): String {
        return "CheckOut Price = " + productList.toString()
    }
}