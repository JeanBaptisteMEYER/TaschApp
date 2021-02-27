package com.jbm.intactchallenge

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jbm.intactchallenge.databinding.FragmentDetailBinding
import com.jbm.intactchallenge.utils.Constants
import com.jbm.intactchallenge.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    val TAG: String =  "tag.jbm." + this::class.java.simpleName

    private val args: DetailFragmentArgs by navArgs()

    val mainViewModel: MainViewModel by activityViewModels()

    lateinit var binding: FragmentDetailBinding

    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // create our binding and set our product
        binding = FragmentDetailBinding.inflate(LayoutInflater.from(context),
            container, false)

        // set and get the product that has been selected
        mainViewModel.setLiveProductById(args.productId)
        binding.product = mainViewModel.liveProduct.value

        // set actionbar title with product title
        requireActivity().title = mainViewModel.liveProduct.value?.title

        binding.setClickListener { view -> onWishListClick(view) }

        return binding.root
    }

    fun onWishListClick(view: View) {
        if ((view as Button).text.equals(getString(R.string.add_to_wishlist)))
            mainViewModel.addToWishList()
        else
            mainViewModel.removeFromWishList()

        findNavController().popBackStack()
    }
}