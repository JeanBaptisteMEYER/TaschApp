package com.jbm.intactchallenge.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jbm.intactchallenge.R
import com.jbm.intactchallenge.model.Product
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class HomeCatalogAdapter @Inject constructor(@ApplicationContext val context: Context): RecyclerView.Adapter<HomeCatalogAdapter.HomeViewHolder>() {

    var productList = mutableListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = HomeViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.catalog_item, parent, false))

        return view
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val product = productList[position]

        holder.itemID = product.id

        //Update item title
        holder.itemTextView.text = product.title

        //load image into ImageView
        Glide
                .with(context)
                .load(product.imageUrl)
                .centerCrop()
                .override(200, 200)
                .into(holder.itemImageView);
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemTextView: TextView
        val itemImageView: ImageView
        var itemID = 0

        init {
            // Define click listener for the ViewHolder's View.
            itemTextView = view.findViewById(R.id.catalog_item_title)
            itemImageView = view.findViewById(R.id.catalog_item_image)
        }
    }
}