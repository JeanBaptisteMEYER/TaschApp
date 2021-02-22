package com.jbm.intactchallenge.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.jbm.intactchallenge.R
import com.jbm.intactchallenge.databinding.DetailFragmentBinding
import com.jbm.intactchallenge.model.Catalog
import com.jbm.intactchallenge.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {
    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    @Inject lateinit var catalog: Catalog
    private var productId = 0

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
        // create our binding and set our product
        binding = DetailFragmentBinding.inflate(LayoutInflater.from(context),
            container, false)
        // get the product that has been selected
        binding.product = catalog.getProdctById(productId)

        // set actionbar title with product title
        requireActivity().title = binding.product!!.title

        // load image with Glide
        Glide
            .with(this)
            .load(binding.product!!.imageUrl)
            .centerCrop()
            .override(500, 500)
            .into(binding.root.findViewById(R.id.detail_image));

        return binding.root
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