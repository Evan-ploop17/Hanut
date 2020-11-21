package com.example.hanut.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.hanut.entities.Product

interface KindProduct {

    @Insert
    fun addProduct(product: Product)

    @Update
    fun updateProduct()

    @Delete
    fun delete()

}