package com.example.hanut.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "Product",
        // necesita saber a que entidad pertenece, como se llama en esa entidad y como se llamará en esta
        foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["idUser"], childColumns = ["idUser "])
        ]
    )
data class Product(

    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "idProduct")
    val idProduct: Int,

    @ColumnInfo(name = "name")
    @NotNull
    val name: String,

    @ColumnInfo(name = "description")
    @NotNull
    val description: String,

    @ColumnInfo(name = "price")
    @NotNull
    val price: Int,

    @ColumnInfo(name = "size")
    @NotNull
    val size: String,

    @ColumnInfo(name = "measurements:")
    val measurements: String,

    // Foreign Key
    @ColumnInfo(name = "idUser")
    @NotNull
    val idUser: String
)