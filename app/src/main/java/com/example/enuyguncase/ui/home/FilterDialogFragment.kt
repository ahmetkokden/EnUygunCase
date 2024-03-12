package com.example.enuyguncase.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.enuyguncase.R
import com.example.enuyguncase.databinding.DialogFragmentFilterBinding
import com.example.enuyguncase.domain.model.FilterResult

class FilterDialogFragment(private val setApplyClickListener: (FilterResult) -> Unit, private val categories: List<String>) : DialogFragment() {

    private var _binding: DialogFragmentFilterBinding? = null
    private val binding get() = _binding!!

    private var isExpandCategories = false
    private var isExpandPrice = false
    private var isExpandDiscount = false

    private var selectedCategories = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,
            android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return bindView(inflater, container)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initVMObservers()
    }

    private fun bindView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = DialogFragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initUI() {
        with(binding) {
            ivLeftIcon.setOnClickListener {
                this@FilterDialogFragment.dismiss()
            }
            tvApplyButton.setOnClickListener {
                val filterResult = FilterResult(
                    minPrice = if (etMinPrice.text.isNotBlank() && etMinPrice.text.isNotEmpty()) Integer.parseInt(etMinPrice.text.toString()) else null,
                    maxPrice = if (etMaxPrice.text.isNotBlank() && etMaxPrice.text.isNotEmpty()) Integer.parseInt(etMaxPrice.text.toString()) else null,
                    minDiscount =if (etMinDiscount.text.isNotBlank() && etMinDiscount.text.isNotEmpty()) Integer.parseInt(etMinDiscount.text.toString()) else null
                )
                setApplyClickListener(filterResult)
                this@FilterDialogFragment.dismiss()
            }

            llCategories.setOnClickListener {
                isExpandCategories = !isExpandCategories
                ivExpandCategories.setImageResource(if (isExpandCategories) R.drawable.ic_expand_more else R.drawable.ic_expand_less)
                //lvCategories.visibility = if(isExpandCategories) View.VISIBLE else View.GONE
            }

            llPrice.setOnClickListener {
                isExpandPrice = !isExpandPrice
                ivExpandPrice.setImageResource(if (isExpandPrice) R.drawable.ic_expand_more else R.drawable.ic_expand_less)
                llExpandPrice.visibility = if(isExpandPrice) View.VISIBLE else View.GONE
            }

            llDiscount.setOnClickListener {
                isExpandDiscount = !isExpandDiscount
                ivExpandDiscount.setImageResource(if (isExpandDiscount) R.drawable.ic_expand_more else R.drawable.ic_expand_less)
                llExpandDiscount.visibility = if(isExpandDiscount) View.VISIBLE else View.GONE
            }

            ivExpandCategories.setImageResource(if (isExpandCategories) R.drawable.ic_expand_more else R.drawable.ic_expand_less)
            ivExpandPrice.setImageResource(if (isExpandPrice) R.drawable.ic_expand_more else R.drawable.ic_expand_less)
            ivExpandDiscount.setImageResource(if (isExpandDiscount) R.drawable.ic_expand_more else R.drawable.ic_expand_less)

            lvCategories.setOnItemClickListener { _, _, position, _ ->
                selectedCategories.add(categories[position])
            }
        }
        setCategoriesList()
    }

    private fun initVMObservers() {

    }

    private fun setCategoriesList(){
        val categoriesAdapter :ArrayAdapter<String> = FilterAdapter(requireContext(),categories)
        binding.lvCategories.adapter  = categoriesAdapter
    }

    companion object {
        fun newInstance(setApplyClickListener: (FilterResult) -> Unit, categories:List<String>
        ): FilterDialogFragment {
            return FilterDialogFragment(setApplyClickListener, categories)
        }
    }
}