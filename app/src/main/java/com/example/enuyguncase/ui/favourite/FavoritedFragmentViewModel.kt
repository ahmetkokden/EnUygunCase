package com.example.enuyguncase.ui.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class FavoritedFragmentViewModel @Inject constructor(
    private val productDatabaseRepository: ProductDatabaseRepository
) : ViewModel() {

    private val _product = MutableSharedFlow<List<FavoriteProductListEntity>?>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val product = _product.asSharedFlow()

    fun getFavoritedProduct() {
        viewModelScope.launch {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val favoritedList = productDatabaseRepository.getFavoritedProducts()
                    _product.emit(favoritedList)
                }
            }
        }
    }

    fun addCartFavoritedProduct(item:FavoriteProductListEntity) {
        viewModelScope.launch {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    productDatabaseRepository.addBasketProduct(item)
                }
            }
        }
    }

    fun unFavoritedProduct(itemId:Long) {
        viewModelScope.launch {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    productDatabaseRepository.deleteFavoritedProduct(itemId)
                    getFavoritedProduct()
                }
            }
        }
    }
}