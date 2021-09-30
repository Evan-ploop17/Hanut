package com.example.hanut.dao

import androidx.room.*
import com.example.hanut.entities.Product


@Dao
interface ProductDAO {

    @Insert
    fun insert(product: Product)

    @Query("SELECT * FROM Product")
    fun getAllProducts(): List<Product>

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)



}