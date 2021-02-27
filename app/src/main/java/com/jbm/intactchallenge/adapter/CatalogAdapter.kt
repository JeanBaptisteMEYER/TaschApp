package com.jbm.intactchallenge.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jbm.intactchallenge.R
import com.jbm.intactchallenge.databinding.ListItemCatalogBinding
import com.jbm.intactchallenge.model.Catalog
import com.jbm.intactchallenge.model.Product


class CatalogAdapter (val context: Context): RecyclerView.Adapter<CatalogAdapter.HomeViewHolder>() {

    var catalog = Catalog()

    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val catalogItemBinding = ListItemCatalogBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)

        return HomeViewHolder(catalogItemBinding)
    }

    @Override
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val product = catalog.productList.get(position)

        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return catalog.productList.size
    }

    class HomeViewHolder(val catalogItemBinding: ListItemCatalogBinding):
            RecyclerView.ViewHolder(catalogItemBinding.root) {

        fun bind(product: Product) {
            catalogItemBinding.product = product
            catalogItemBinding.executePendingBindings()
        }
    }
}