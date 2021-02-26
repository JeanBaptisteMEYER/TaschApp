package com.jbm.intactchallenge

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.jbm.intactchallenge.databinding.FragmentDetailBinding
import com.jbm.intactchallenge.utils.Constants
import com.jbm.intactchallenge.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    private var productId = 0

    val mainViewModel: MainViewModel by activityViewModels()

    lateinit var binding: FragmentDetailBinding

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

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.liveProduct.observe(viewLifecycleOwner, { product ->
            Log.d(TAG, "LiveProduct Changed = " + product.toString())
        })
    }

    //Initialise all views element.
    fun bindView(inflater: LayoutInflater, container: ViewGroup?): View {
        // create our binding and set our product
        binding = FragmentDetailBinding.inflate(LayoutInflater.from(context),
            container, false)

        mainViewModel.setProdctById(productId)

        // get the product that has been selected
        binding.product = mainViewModel.liveProduct.value

        // set actionbar title with product title
        requireActivity().title = mainViewModel.liveProduct.value?.title

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
            mainViewModel.addToWishList()
        else
            mainViewModel.removeFromWishList()

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