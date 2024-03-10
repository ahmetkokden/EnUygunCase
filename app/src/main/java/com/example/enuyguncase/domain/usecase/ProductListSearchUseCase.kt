package com.example.enuyguncase.domain.usecase

import com.example.enuyguncase.data.base.NetworkResult
import com.example.enuyguncase.domain.model.ProductList
import kotlinx.coroutines.flow.Flow

interface ProductListSearchUseCase {

    suspend fun searchProduct(searchKeyword:String) : Flow<NetworkResult<ProductList>>
}