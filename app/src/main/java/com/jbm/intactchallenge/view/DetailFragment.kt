package com.jbm.intactchallenge.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.jbm.intactchallenge.MainActivity
import com.jbm.intactchallenge.R
import com.jbm.intactchallenge.model.Constantes


/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {
    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    private var productId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getInt(Constantes().ID_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.product_detail_fragment, container, false)

        val product = (activity as MainActivity).myRepository.getPoductByID(productId)

        Glide
            .with(this)
            .load(product.imageUrl)
            .centerCrop()
            .override(500, 500)
            .into(view.findViewById<ImageView>(R.id.detail_image));

        view.findViewById<TextView>(R.id.detail_price).text = "$" + product.price
        view.findViewById<TextView>(R.id.detail_description).text = product.fullDescription

        // add the colored square
        val colorLayout =
            view.findViewById<LinearLayout>(R.id.detail_color_layout)

        for (color in product.colors) {
            val colorView =
                layoutInflater.inflate(R.layout.product_color_view, colorLayout, false)
            colorView.setBackgroundColor(android.graphics.Color.parseColor(color.code))

            colorLayout.addView(colorView)
        }

        view.findViewById<TextView>(R.id.detail_size).text = "H: " + product.size.height + "\n" +
                "W: " + product.size.width + "\n" +
                "D: " + product.size.depth

        val wishlistButton = view.findViewById<Button>(R.id.detail_add_wishlist_button)
        if (product.wishListed == 0) {
            wishlistButton.text = getString(R.string.add_to_wishlist)
            wishlistButton.setBackgroundColor(android.graphics.Color.parseColor("#EC3331"))
        } else {
            wishlistButton.text = getString(R.string.remove_to_wishlist)
            wishlistButton.setBackgroundColor(android.graphics.Color.parseColor("#000000"))
        }

        wishlistButton.setOnClickListener {
            if ((it as Button).text.equals(getString(R.string.add_to_wishlist))) {
                product.wishListed = 1
                wishlistButton.text = getString(R.string.remove_to_wishlist)
                wishlistButton.setBackgroundColor(android.graphics.Color.parseColor("#000000"))
            } else {
                product.wishListed = 0
                wishlistButton.text = getString(R.string.add_to_wishlist)
                wishlistButton.setBackgroundColor(android.graphics.Color.parseColor("#EC3331"))
            }
        }

        // set actionbar title with product title
        requireActivity().title = product.title

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(productId: Int) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(Constantes().ID_PARAM, productId)
                }
            }
    }
}