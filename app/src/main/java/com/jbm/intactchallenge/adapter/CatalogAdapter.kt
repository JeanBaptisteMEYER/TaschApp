package com.jbm.intactchallenge.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.jbm.intactchallenge.HomeFragmentDirections
import com.jbm.intactchallenge.databinding.ListItemCatalogBinding
import com.jbm.intactchallenge.model.Catalog
import com.jbm.intactchallenge.model.Product


class CatalogAdapter (val context: Context): RecyclerView.Adapter<CatalogAdapter.HomeViewHolder>() {

    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    var catalog = Catalog()

    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val catalogItemBinding = ListItemCatalogBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)

        return HomeViewHolder(catalogItemBinding)
    }

    @Override
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val product = catalog.products.get(position)

        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return catalog.products.size
    }

    class HomeViewHolder(val catalogItemBinding: ListItemCatalogBinding):
            RecyclerView.ViewHolder(catalogItemBinding.root) {
        init {
            catalogItemBinding.setClickListener { view ->
                catalogItemBinding.product?.let {
                    navToDetail(view, it.id)
                }
            }
        }

        fun navToDetail(view: View, productId: Int) {
            val direction = HomeFragmentDirections.actionShowDetailfragment(productId)
            view.findNavController().navigate(direction)
        }

        fun bind(product: Product) {
            catalogItemBinding.product = product
            catalogItemBinding.executePendingBindings()
        }
    }
}