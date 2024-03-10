package com.example.enuyguncase.db

import androidx.room.Dao
import androidx.room.Query
import com.example.enuyguncase.data.local.FavoriteProductListEntity

@Dao
interface FavoriteProductDao {
    @Query("SELECT * FROM favorite_list_table LIMIT 1")
    fun getFavoriteProducts(): List<FavoriteProductListEntity>

    @Query("DELETE FROM favorite_list_table where id=:productId")
    fun deleteFavoriteProduct(productId: Long)
}