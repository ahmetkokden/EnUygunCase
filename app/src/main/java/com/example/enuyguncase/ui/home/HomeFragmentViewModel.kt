package com.example.enuyguncase.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enuyguncase.data.base.NetworkResult
import com.example.enuyguncase.domain.model.FilterResult
import com.example.enuyguncase.domain.model.ProductListItem
import com.example.enuyguncase.domain.usecase.ProductListSearchUseCase
import com.example.enuyguncase.domain.usecase.ProductListUseCase
import com.example.enuyguncase.utilities.SortEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val productListUseCase: ProductListUseCase,
    private val productListSearchUseCase: ProductListSearchUseCase,
) : ViewModel() {

    private val _productList = MutableStateFlow<List<ProductListItem>>(emptyList())
    val productList = _productList.asStateFlow()

    private val _productCategories = MutableStateFlow<List<String>>(emptyList())
    val productCategories = _productCategories.asStateFlow()

    fun getProductList() {
        viewModelScope.launch {
            productListUseCase.getProductList().collect { response ->
                when (response.status) {
                    NetworkResult.Status.SUCCESS -> {
                        _productList.emit(response.data?.products ?: emptyList())
                    }

                    NetworkResult.Status.LOADING -> {

                    }

                    NetworkResult.Status.ERROR -> {

                    }
                }
            }
        }
    }

    fun getProductCategories() {
        viewModelScope.launch {
            productListUseCase.getProductCategories().collect { response ->
                when (response.status) {
                    NetworkResult.Status.SUCCESS -> {
                        _productCategories.emit(response.data?.toList() ?: emptyList())
                    }

                    NetworkResult.Status.LOADING -> {

                    }

                    NetworkResult.Status.ERROR -> {

                    }
                }
            }
        }
    }

    fun searchProduct(searchKeyword: CharSequence) {
        viewModelScope.launch {
            productListSearchUseCase.searchProduct(searchKeyword.toString()).collect { response ->
                when (response.status) {
                    NetworkResult.Status.SUCCESS -> {
                        _productList.emit(response.data?.products ?: emptyList())
                    }

                    NetworkResult.Status.LOADING -> {

                    }

                    NetworkResult.Status.ERROR -> {

                    }
                }
            }
        }
    }

    fun sortProductAboutEvent(sortEvent: SortEvent) {
        when (sortEvent) {
            SortEvent.DECREASING_PRICE -> {
                sortForDecreasingPrice()
            }

            SortEvent.INCREASING_PRICE -> {
                sortForIncreasingPrice()
            }

            SortEvent.MOST_DISCOUNT -> {
                sortForMostDiscount()
            }
        }
    }

    fun filterProduct(filterResult: FilterResult) {
        var filteredProductList = productList.value.toMutableList()

        filterResult.maxPrice?.let { price ->
            filteredProductList =
                filteredProductList.filter { (it.finalPrice ?: 0.0) < price }.toMutableList()
        }

        filterResult.minPrice?.let { price ->
            filteredProductList =
                filteredProductList.filter { (it.finalPrice ?: 0.0) > price }.toMutableList()
        }

        filterResult.minDiscount?.let { discount ->
            filteredProductList =
                filteredProductList.filter { (it.discountPercentage ?: 0.0) > discount }
                    .toMutableList()
        }

        if (filterResult.maxPrice == null && filterResult.minPrice == null && filterResult.minDiscount == null) {
            getProductList()
        }

        viewModelScope.launch {
            _productList.emit(filteredProductList)
        }
    }

    private fun sortForDecreasingPrice() {
        viewModelScope.launch {
            _productList.emit(
                productList.value.sortedByDescending { it.finalPrice }
            )
        }
    }

    private fun sortForIncreasingPrice() {
        viewModelScope.launch {
            _productList.emit(
                productList.value.sortedBy { it.finalPrice }
            )
        }
    }

    private fun sortForMostDiscount() {
        viewModelScope.launch {
            _productList.emit(
                productList.value.sortedByDescending { it.discountPercentage }
            )
        }
    }
}