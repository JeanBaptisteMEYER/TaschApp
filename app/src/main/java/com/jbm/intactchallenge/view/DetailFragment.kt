package com.jbm.intactchallenge.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.jbm.intactchallenge.R
import com.jbm.intactchallenge.adapter.HomeCatalogAdapter
import com.jbm.intactchallenge.databinding.CatalogItemBinding
import com.jbm.intactchallenge.databinding.DetailFragmentBinding
import com.jbm.intactchallenge.utils.Constants
import com.jbm.intactchallenge.model.MyRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class DetailFragment : Fragment() {
    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    private var productId: Int = 0

    @Inject lateinit var myRepository: MyRepository
    @Inject lateinit var mAdapter: HomeCatalogAdapter

    lateinit var binding: DetailFragmentBinding

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the ID of the selected product from argument
        arguments?.let {
            productId = it.getInt(Constants().ID_PARAM)
        }
    }

    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Our initView Function will build our view and returns it
        return bindView(inflater, container)
    }

    //Initialise all views element.
    fun bindView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DetailFragmentBinding.inflate(LayoutInflater.from(context),
            container, false)

        // get the product that has been selected
        binding.product = mAdapter.getProductById(productId)

        Glide
            .with(this)
            .load(binding.product!!.imageUrl)
            .centerCrop()
            .override(500, 500)
            .into(binding.root.findViewById<ImageView>(R.id.detail_image));

        // add the colored square
        val colorLayout =
            binding.root.findViewById<LinearLayout>(R.id.detail_color_layout)

        for (color in binding.product!!.colors) {
            val colorView =
                layoutInflater.inflate(R.layout.color_view_big, colorLayout, false)

            val background = resources.getDrawable(R.drawable.round_corner_color_shape)
            background.setTint(android.graphics.Color.parseColor(color.code))
            colorView.background = background

            colorLayout.addView(colorView)
        }

        // add to wish list button setup and listener
        val wishlistButton = binding.root.findViewById<Button>(R.id.detail_add_wishlist_button)

        if (binding.product!!.wishListed == 0) {
            wishlistButton.text = getString(R.string.add_to_wishlist)
            wishlistButton.setBackgroundColor(android.graphics.Color.parseColor("#EC3331"))
        } else {
            wishlistButton.text = getString(R.string.remove_to_wishlist)
            wishlistButton.setBackgroundColor(android.graphics.Color.parseColor("#000000"))
        }

        //set on ratingChangeListener to our ratingBar
        binding.root.findViewById<RatingBar>(R.id.detail_rating_start).setOnRatingBarChangeListener {
                ratingBar, fl, b ->
            onRatingBarChange(fl)
        }

        // set actionbar title with product title
        requireActivity().title = binding.product!!.title

        return binding.root
    }

    fun onRatingBarChange (rating: Float) {
        binding.product!!.rating = rating
    }

    fun onWishListClick(view: View) {
        if ((view as Button).text.equals(getString(R.string.add_to_wishlist)))
            binding.product!!.wishListed = 1
        else
            binding.product!!.wishListed = 0

        fragmentManager?.popBackStack()
    }

    companion object {
        @JvmStatic
        fun newInstance(productId: Int) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(Constants().ID_PARAM, productId)
                }
            }
    }
}