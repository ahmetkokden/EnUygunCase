package com.example.enuyguncase.ui.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enuyguncase.data.base.NetworkResult
import com.example.enuyguncase.domain.model.ProductListItem
import com.example.enuyguncase.domain.usecase.ProductListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailFragmentViewModel @Inject constructor(
    private val productListUseCase: ProductListUseCase
) : ViewModel() {

    private val _product = MutableSharedFlow<ProductListItem?>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val product = _product.asSharedFlow()

    fun getProductDetail(productId: Int) {
        viewModelScope.launch {
            productListUseCase.getProduct(productId).collect { response ->
                when (response.status) {
                    NetworkResult.Status.SUCCESS -> {
                        _product.emit(response.data)
                    }

                    NetworkResult.Status.LOADING -> {

                    }

                    NetworkResult.Status.ERROR -> {

                    }
                }
            }
        }
    }

    fun setProductFavoritedStatus(isFavorited: Boolean) {
        viewModelScope.launch {
            product.replayCache.last().apply {
                this?.isFavorited = isFavorited
            }.run {
                _product.emit(this)
            }
        }
    }
}