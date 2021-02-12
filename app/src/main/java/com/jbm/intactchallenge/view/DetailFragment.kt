package com.jbm.intactchallenge.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.jbm.intactchallenge.MainActivity
import com.jbm.intactchallenge.R
import com.jbm.intactchallenge.model.Constantes
import kotlin.math.roundToInt


class DetailFragment : Fragment() {
    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    private var productId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // the ID of the selected product
            productId = it.getInt(Constantes().ID_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Our initView Function will build our view and returns it
        return initView(inflater.inflate(R.layout.detail_fragment, container, false))
    }

    fun initView(view: View): View {
        // get the product that has been selected
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
                layoutInflater.inflate(R.layout.color_view_big, colorLayout, false)

            val background = resources.getDrawable(R.drawable.round_corner_color_shape)
            background.setTint(android.graphics.Color.parseColor(color.code))
            colorView.background = background

            colorLayout.addView(colorView)
        }

        ("H: " + product.size.height + "\n" +
                "W: " + product.size.width + "\n" +
                "D: " + product.size.depth).also { view.findViewById<TextView>(R.id.detail_size).text = it }

        // add to wish list button setup and listener
        val wishlistButton = view.findViewById<Button>(R.id.detail_add_wishlist_button)

        if (product.wishListed == 0) {
            wishlistButton.text = getString(R.string.add_to_wishlist)
            wishlistButton.setBackgroundColor(android.graphics.Color.parseColor("#EC3331"))
        } else {
            wishlistButton.text = getString(R.string.remove_to_wishlist)
            wishlistButton.setBackgroundColor(android.graphics.Color.parseColor("#000000"))
        }

        wishlistButton.setOnClickListener {
            if ((it as Button).text.equals(getString(R.string.add_to_wishlist)))
                product.wishListed = 1
            else
                product.wishListed = 0

            fragmentManager?.popBackStack()
        }

        val ratingStar = view.findViewById<RatingBar>(R.id.detail_rating_start)
        ratingStar.rating = product.rating

        ratingStar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            Log.d(TAG, fl.toString() + "   " + fl.roundToInt().toString())
            product.rating = fl
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