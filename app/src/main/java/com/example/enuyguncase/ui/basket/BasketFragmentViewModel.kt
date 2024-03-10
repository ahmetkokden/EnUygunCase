package com.example.enuyguncase.ui.basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enuyguncase.data.local.BasketProductListEntity
import com.example.enuyguncase.data.local.FavoriteProductListEntity
import com.example.enuyguncase.domain.repository.ProductDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BasketFragmentViewModel @Inject constructor(
    private val productDatabaseRepository: ProductDatabaseRepository
) : ViewModel() {

    private val _product = MutableSharedFlow<List<BasketProductListEntity>?>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val product = _product.asSharedFlow()

    fun getBasketProduct() {
        viewModelScope.launch {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val basketProductList = productDatabaseRepository.getBasketProducts()
                    _product.emit(basketProductList)
                }
            }
        }
    }
}