package com.example.enuyguncase.domain.repository

import com.example.enuyguncase.data.local.FavoriteProductListEntity
import com.example.enuyguncase.db.ProductDatabase
import com.example.enuyguncase.domain.mapper.toBasketProductListEntity
import com.example.enuyguncase.domain.mapper.toFavoriteProductListEntity
import com.example.enuyguncase.domain.model.ProductListItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductDatabaseRepository @Inject internal constructor(
    private val productDatabase: ProductDatabase
) {
    private val basketProductDao = productDatabase.basketProductDao()

    fun getBasketProducts() = basketProductDao.getBasketProducts()

    fun addBasketProduct(favoriteProductListEntity: FavoriteProductListEntity) =
        basketProductDao.addBasketProduct(favoriteProductListEntity.toBasketProductListEntity())


    fun updateBasketProductCount(count: Int, productId: Long) =
        basketProductDao.updateCount(count, productId)

    fun deleteProductFromBasket(productId: Long) =
        basketProductDao.deleteBasketProduct(productId)

    private val favoriteProductDao = productDatabase.favoriteProductDao()

    fun getFavoritedProducts() = favoriteProductDao.getFavoriteProducts()

    fun getFavoritedProduct(productId: Long) = favoriteProductDao.getFavoriteProduct(productId)

    fun addFavoriteProduct(productListItem: ProductListItem) =
        favoriteProductDao.addFavoriteProduct(productListItem.toFavoriteProductListEntity())

    fun deleteFavoritedProduct(productId: Long) =
        favoriteProductDao.deleteFavoriteProduct(productId)
}