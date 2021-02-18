package com.jbm.intactchallenge.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jbm.intactchallenge.R
import com.jbm.intactchallenge.databinding.CatalogItemBinding
import com.jbm.intactchallenge.model.Product
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class HomeCatalogAdapter @Inject constructor(@ApplicationContext val context: Context):
        RecyclerView.Adapter<HomeCatalogAdapter.HomeViewHolder>() {

    var productList = mutableListOf<Product>()

    fun getProductById(productId: Int): Product {
        for (p in productList)
            if (p.id == productId)
                return p
        return Product(-1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val catalogItemBinding = CatalogItemBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)

        return HomeViewHolder(catalogItemBinding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)

        Glide
            .with(context)
            .load(product.imageUrl)
            .centerCrop()
            .override(200, 200)
            .into(holder.catalogItemBinding.root.findViewById(R.id.catalog_item_image));
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class HomeViewHolder(val catalogItemBinding: CatalogItemBinding):
            RecyclerView.ViewHolder(catalogItemBinding.root) {

        fun bind(product: Product) {
            catalogItemBinding.product = product
            catalogItemBinding.executePendingBindings()
        }
    }
}