package com.example.enuyguncase.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enuyguncase.data.base.NetworkResult
import com.example.enuyguncase.domain.model.ProductListItem
import com.example.enuyguncase.domain.usecase.ProductListSearchUseCase
import com.example.enuyguncase.domain.usecase.ProductListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val productListUseCase: ProductListUseCase,
    private val productListSearchUseCase: ProductListSearchUseCase,
) : ViewModel(){

    private val _productList = MutableStateFlow<List<ProductListItem>>(emptyList())
    val productList = _productList.asStateFlow()

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

    fun searchProduct(searchKeyword:CharSequence){
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
}