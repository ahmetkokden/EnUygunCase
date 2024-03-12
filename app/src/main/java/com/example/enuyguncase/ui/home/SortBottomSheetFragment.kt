package com.example.enuyguncase.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.enuyguncase.databinding.BottomSheetHomeSortBinding
import com.example.enuyguncase.ui.component.BaseBottomSheetFragment
import com.example.enuyguncase.utilities.SortEvent

class SortBottomSheetFragment(private val selectOnClickListener: (SortEvent) -> Unit) :
    BaseBottomSheetFragment() {

    private var _binding: BottomSheetHomeSortBinding? = null
    private val binding get() = _binding!!
    override fun bindView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = BottomSheetHomeSortBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initUI() {
        with(binding) {
            tvDescreasingPrice.setOnClickListener {
                selectOnClickListener(SortEvent.DECREASING_PRICE)
                this@SortBottomSheetFragment.dismiss()
            }
            tvIncreasingPrice.setOnClickListener {
                selectOnClickListener(SortEvent.INCREASING_PRICE)
                this@SortBottomSheetFragment.dismiss()
            }

            tvMostDiscount.setOnClickListener {
                selectOnClickListener(SortEvent.MOST_DISCOUNT)
                this@SortBottomSheetFragment.dismiss()
            }
        }
    }

    override fun initVMObservers() = Unit

    companion object {
        fun newInstance(
            selectOnClickListener: (SortEvent) -> Unit
        ): SortBottomSheetFragment {
            return SortBottomSheetFragment(selectOnClickListener)
        }
    }
}