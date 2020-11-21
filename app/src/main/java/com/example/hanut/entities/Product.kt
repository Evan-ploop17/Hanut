package com.example.hanut.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(
    tableName = "Product",
    foreignKeys = [
        ForeignKey(entity = KindProduct::class, parentColumns = ["idKindProduct"], childColumns = ["kindProduct"])
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
    @ColumnInfo(name = "kindProduct")
    @NotNull
    val kindProduct: String
)