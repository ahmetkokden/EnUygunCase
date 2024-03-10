package com.example.enuyguncase.domain.repository

import android.util.Log
import com.example.enuyguncase.data.ProductService
import com.example.enuyguncase.data.base.BaseErrorResponse
import com.example.enuyguncase.data.base.NetworkResult
import com.example.enuyguncase.domain.mapper.toProductList
import com.example.enuyguncase.domain.mapper.toProductListItem
import com.example.enuyguncase.domain.model.ProductList
import com.example.enuyguncase.domain.model.ProductListItem
import com.example.enuyguncase.utilities.Util.Companion.stringSuspending
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productService: ProductService
) : ProductRepository {
    override suspend fun getProductList(): Flow<NetworkResult<ProductList>> {
        return flow {
            try {
                val response = productService.getProductList()
                val mappedData = response.toProductList()
                emit(NetworkResult.success(mappedData))
            } catch (e: HttpException) {
                val type = object : TypeToken<BaseErrorResponse>() {}.type
                val errorResponse: BaseErrorResponse =
                    Gson().fromJson(e.response()?.errorBody()?.stringSuspending(), type)
                emit(NetworkResult.error(errorResponse.message, errorResponse.code))
            }
        }.flowOn(Dispatchers.IO)
            .onStart { emit(NetworkResult.loading()) }
    }

    override suspend fun getProduct(id: Int): Flow<NetworkResult<ProductListItem>> {
        return flow {
            try {
                val response = productService.getProduct(id)
                val mappedData = response.toProductListItem()
                emit(NetworkResult.success(mappedData))
            } catch (e: HttpException) {
                val type = object : TypeToken<BaseErrorResponse>() {}.type
                val errorResponse: BaseErrorResponse =
                    Gson().fromJson(e.response()?.errorBody()?.stringSuspending(), type)
                emit(NetworkResult.error(errorResponse.message, errorResponse.code))
            }
        }.flowOn(Dispatchers.IO)
            .onStart { emit(NetworkResult.loading()) }
    }

    override suspend fun searchProduct(searchKeyword:String): Flow<NetworkResult<ProductList>> {
        return flow {
            try {
                val response = productService.searchProduct(searchKeyword)
                val mappedData = response.toProductList()
                emit(NetworkResult.success(mappedData))
            } catch (e: HttpException) {
                val type = object : TypeToken<BaseErrorResponse>() {}.type
                val errorResponse: BaseErrorResponse =
                    Gson().fromJson(e.response()?.errorBody()?.stringSuspending(), type)
                emit(NetworkResult.error(errorResponse.message, errorResponse.code))
            }
        }.flowOn(Dispatchers.IO)
            .onStart { emit(NetworkResult.loading()) }
    }
}