package com.example.enuyguncase.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.enuyguncase.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {
    private var _isUpdateBasket = MutableLiveData<Boolean>()
    val isUpdateBasket = MutableLiveData<Boolean>()
    private var _isUpdateFavorite = MutableLiveData<Boolean>()
    val isUpdateFavorite = MutableLiveData<Boolean>()
    fun sendEvent(event:Event) {
        when(event) {
            Event.UpdateFavorite -> {
                _isUpdateFavorite.value = true
            }
            Event.UpdateBasket -> {
                _isUpdateBasket.value = true
            }
        }
    }

    fun restartBasketStatus(){
        _isUpdateBasket.value = false
    }

    fun restartFavoriteStatus(){
        _isUpdateFavorite.value = false
    }
}