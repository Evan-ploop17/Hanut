package com.example.hanut.dao

import androidx.room.*
import com.example.hanut.entities.Product

@Dao
interface Purchase {

    @Insert
    fun insert(purchase: Purchase)

    @Query("SELECT * FROM Product")
    fun getAllProducts(): List<Product>

    @Update
    fun update(purchase: Purchase)

    @Delete
    fun delete(purchase: Purchase)



}