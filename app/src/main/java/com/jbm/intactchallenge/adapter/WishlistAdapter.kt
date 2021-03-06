package com.jbm.intactchallenge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.jbm.intactchallenge.HomeFragmentDirections
import com.jbm.intactchallenge.databinding.ListItemWishlistBinding
import com.jbm.intactchallenge.model.Product


class WishlistAdapter (): Adapter<WishlistAdapter.WishlistViewHolder>() {

    var wishlist = mutableListOf<Product>()

    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val wishlistItemBinding = ListItemWishlistBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)

        return WishlistViewHolder(wishlistItemBinding)
    }

    @Override
    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val product = wishlist.get(position)

        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return wishlist.size
    }

    class WishlistViewHolder(val wishlistItemBinding: ListItemWishlistBinding):
            RecyclerView.ViewHolder(wishlistItemBinding.root) {

        init {
            wishlistItemBinding.setClickListener { view ->
                wishlistItemBinding.product?.let {
                    navToDetail(view, it.id)
                }
            }
        }

        fun navToDetail(view: View, productId: Int) {
            val direction = HomeFragmentDirections.actionShowDetailfragment(productId)
            view.findNavController().navigate(direction)
        }

        fun bind(product: Product) {
            wishlistItemBinding.product = product
            wishlistItemBinding.executePendingBindings()
        }
    }
}