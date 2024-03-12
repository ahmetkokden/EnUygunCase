package com.example.enuyguncase.ui.productdetail

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.enuyguncase.R
import com.example.enuyguncase.databinding.FragmentProductDetailBinding
import com.example.enuyguncase.domain.mapper.toImageItemList
import com.example.enuyguncase.domain.model.ProductListItem
import com.example.enuyguncase.navigation.MultiNavHost
import com.example.enuyguncase.ui.adapter.ImageAdapter
import com.example.enuyguncase.ui.home.HomeFragment.Companion.PRODUCT_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailBinding

    private lateinit var viewpager2: ViewPager2
    private lateinit var pageChangeListener: ViewPager2.OnPageChangeCallback

    private var isSetViewPager = false

    private val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    ).apply {
        setMargins(8, 0, 8, 0)
    }

    private val productDetailFragmentViewModel: ProductDetailFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MultiNavHost.bottomNavigationListener.bottomNavVisibility(false)
        initUI()
        observeData()
    }

    private fun initUI() {
        val productId = requireArguments().getInt(PRODUCT_ID)
        productDetailFragmentViewModel.getProductDetail(productId)
        with(binding) {
            ivRightIcon.setOnClickListener {
                productDetailFragmentViewModel.setProductFavoritedStatus(
                    productDetailFragmentViewModel.product.replayCache.last()?.isFavorited?.not()
                        ?: false
                )
            }
            ivLeftIcon.setOnClickListener {
                findNavController().popBackStack()
            }

            tvAddCartButton.setOnClickListener {
                productDetailFragmentViewModel.addCartFavoritedProduct()
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                productDetailFragmentViewModel.product.collectLatest {
                    it?.let {
                        setProductDetailComponents(it)
                        if (isSetViewPager.not()) {
                            setImageSlider(it)
                        }
                    }

                }
            }
        }
    }

    private fun setProductDetailComponents(product: ProductListItem) {
        with(binding) {
            if (product.isFavorited) {
                ivRightIcon.setImageResource(R.drawable.ic_favorite_selected_red)
            } else {
                ivRightIcon.setImageResource(R.drawable.ic_favorite)
            }
            binding.tvProductTitle.text = product.title

            binding.tvProductSubtitle.text = product.title

            binding.tvProductInfo.text = product.description

            binding.tvProductFinalPrice.text = context?.getString(R.string.price, product.displayFinalPrice)

            binding.tvProductPrice.text = context?.getString(R.string.price, product.displayPrice)
            binding.tvProductPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            binding.tvProductDiscount.text = "%" + context?.getString(R.string.product_discount, product.discountPercentage.toString())
        }
    }

    private fun setImageSlider(product: ProductListItem) {
        isSetViewPager = product.productImages.isNullOrEmpty().not()
        viewpager2 = binding.vp2ImageSlider
        val imageList = product.toImageItemList()
        val imageAdapter = ImageAdapter()
        viewpager2.adapter = imageAdapter
        imageAdapter.submitList(imageList)

        val slideDotLL = binding.llImageDots
        val dotsImage = Array(imageList?.size ?: 0) { ImageView(context) }

        dotsImage.forEach {
            it.setImageResource(
                R.drawable.ic_empty_dot
            )
            slideDotLL.addView(it, params)
        }

        // default first dot selected
        dotsImage[0].setImageResource(R.drawable.ic_filled_dot)

        pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                dotsImage.mapIndexed { index, imageView ->
                    if (position == index) {
                        imageView.setImageResource(
                            R.drawable.ic_filled_dot
                        )
                    } else {
                        imageView.setImageResource(R.drawable.ic_empty_dot)
                    }
                }
                super.onPageSelected(position)
            }
        }
        viewpager2.registerOnPageChangeCallback(pageChangeListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewpager2.unregisterOnPageChangeCallback(pageChangeListener)
    }
}