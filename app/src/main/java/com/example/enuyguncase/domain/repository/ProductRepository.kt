package com.example.enuyguncase.domain.repository

import com.example.enuyguncase.data.base.NetworkResult
import com.example.enuyguncase.domain.model.ProductList
import com.example.enuyguncase.domain.model.ProductListItem
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProductList() : Flow<NetworkResult<ProductList>>

    suspend fun getProductCategories() : Flow<NetworkResult<Array<String>>>
    suspend fun getProduct(
        id: Int
    ): Flow<NetworkResult<ProductListItem>>
    suspend fun searchProduct(searchKeyword:String) : Flow<NetworkResult<ProductList>>
}