package com.example.enuyguncase.ui.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.enuyguncase.R
import com.example.enuyguncase.databinding.FragmentBasketBinding
import com.example.enuyguncase.navigation.setBadgeNumber
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BasketFragment : Fragment() {
    private lateinit var binding: FragmentBasketBinding

    private val basketFragmentViewModel: BasketFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_basket, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeData()
    }

    private fun initUI() {
        basketFragmentViewModel.getBasketProduct()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            basketFragmentViewModel.product.collectLatest {
                setBadgeNumber(it?.size ?: 0)
            }
        }
    }
}