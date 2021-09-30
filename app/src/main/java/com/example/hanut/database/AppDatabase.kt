package com.example.hanut.database/*
package com.example.hanut.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hanut.dao.ProductDAO
import com.example.hanut.entities.Product

@Database(entities = [Product::class, User::class], version = 1, exportSchema = false )
abstract class AppDatabase : RoomDatabase() {

    // DAO's que queremos
    abstract val ProductDao: ProductDAO
    abstract val UserDao: UserDAO

    // name database
    companion object{
        const val DATABASE_NAME = "hanut"
    }


}*/
