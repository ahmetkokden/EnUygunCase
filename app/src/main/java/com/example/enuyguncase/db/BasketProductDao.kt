package com.example.enuyguncase.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.enuyguncase.data.local.BasketProductListEntity

@Dao
interface BasketProductDao {
    @Query("SELECT * FROM basket_list_table")
    fun getBasketProducts(): List<BasketProductListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBasketProduct(basketProductListEntity: BasketProductListEntity)

    @Query("DELETE FROM basket_list_table where id=:productId ")
    fun deleteBasketProduct(productId: Long)

    @Query("DELETE FROM basket_list_table")
    fun purgeAllBasketProduct()

    @Query("UPDATE basket_list_table SET product_count=:count WHERE id = :productId")
    fun updateCount(count: Int, productId: Long)
}