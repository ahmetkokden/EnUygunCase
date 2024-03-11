package com.example.enuyguncase.ui.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enuyguncase.data.base.NetworkResult
import com.example.enuyguncase.domain.mapper.toFavoriteProductListEntity
import com.example.enuyguncase.domain.model.ProductListItem
import com.example.enuyguncase.domain.repository.ProductDatabaseRepository
import com.example.enuyguncase.domain.usecase.ProductListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductDetailFragmentViewModel @Inject constructor(
    private val productListUseCase: ProductListUseCase,
    private val productDatabaseRepository: ProductDatabaseRepository
) : ViewModel() {

    private val _product = MutableSharedFlow<ProductListItem?>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val product = _product.asSharedFlow()

    var productData: ProductListItem? = null

    fun getProductDetail(productId: Int) {
        viewModelScope.launch {
            productListUseCase.getProduct(productId).collect { response ->
                when (response.status) {
                    NetworkResult.Status.SUCCESS -> {
                        _product.tryEmit(response.data)
                        productData = response.data
                        getFavoritedProduct()
                    }

                    NetworkResult.Status.LOADING -> {

                    }

                    NetworkResult.Status.ERROR -> {

                    }
                }
            }
        }
    }

    fun getFavoritedProduct() {
        viewModelScope.launch {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val favoritedList = productDatabaseRepository.getFavoritedProducts()
                    favoritedList.forEach {
                        if (it.id == productData?.id) {
                            setProductFavoritedStatus(true)
                        }
                    }
                }
            }
        }
    }

    fun addCartFavoritedProduct() {
        viewModelScope.launch {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    productData?.let { productDatabaseRepository.addBasketProduct(it.toFavoriteProductListEntity()) }
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

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                product.replayCache.last()
                    ?.let {
                        if (isFavorited) {
                            productDatabaseRepository.addFavoriteProduct(it)
                        } else {
                            it.id?.let { it1 -> productDatabaseRepository.deleteFavoritedProduct(it1) }
                        }
                    }
            }
        }
    }
}