package com.jbm.intactchallenge.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jbm.intactchallenge.R
import com.jbm.intactchallenge.databinding.ListItemWishlistBinding
import com.jbm.intactchallenge.model.Product


class WishlistAdapter (val context: Context): RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

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

        Glide
            .with(context)
            .load(product.imageUrl)
            .centerCrop()
            .override(200, 200)
            .into(holder.wishlistItemBinding.root.findViewById(R.id.wishlist_item_image));
    }

    override fun getItemCount(): Int {
        return wishlist.size
    }

    class WishlistViewHolder(val wishlistItemBinding: ListItemWishlistBinding):
            RecyclerView.ViewHolder(wishlistItemBinding.root) {

        fun bind(product: Product) {
            wishlistItemBinding.product = product
            wishlistItemBinding.executePendingBindings()
        }
    }
}