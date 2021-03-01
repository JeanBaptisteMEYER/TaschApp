package com.jbm.intactchallenge.model

import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

class Product {
    @SerializedName("id") var id = 0
    @SerializedName("title") var title = ""
    @SerializedName("brand") var brand = ""
    @SerializedName("short_description")  var shortDescription = ""
    @SerializedName("description") var fullDescription = ""
    @SerializedName("price") var price = 0.0
    @SerializedName("image") var imageUrl = ""
    @SerializedName("colors") var colors = listOf<Color>()
    @SerializedName("size") var size = Size()
    @SerializedName("quantity") var quantityInStock = 0

    var wishListed = 0
    var rating = 0.toFloat()

    fun getColorSize(): Int {
        colors?.let { return colors.size }

        return 0
    }

    fun getRoundedPrice(): Int {
        return price.roundToInt()
    }

    @Override
    override fun toString(): String {
        return "ID = " + id + " Title = " + title + " Quantity = " + quantityInStock + ", wishlisted = " + wishListed + ", Rating = " + rating
    }
}