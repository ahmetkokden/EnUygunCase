package com.example.enuyguncase.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.enuyguncase.data.local.FavoriteProductListEntity

@Dao
interface FavoriteProductDao {
    @Query("SELECT * FROM favorite_list_table")
    fun getFavoriteProducts(): List<FavoriteProductListEntity>

    @Query("SELECT * FROM favorite_list_table where id=:productId LIMIT 1")
    fun getFavoriteProduct(productId: Long):FavoriteProductListEntity

    @Query("DELETE FROM favorite_list_table where id=:productId")
    fun deleteFavoriteProduct(productId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavoriteProduct(favoriteProductListEntity: FavoriteProductListEntity)
}