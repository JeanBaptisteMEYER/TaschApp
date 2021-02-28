package com.jbm.intactchallenge.model

class Catalog {
    var products = listOf<Product>()

    @Override
    override fun toString(): String {
        return "Catalog = " + products.toString()
    }
}