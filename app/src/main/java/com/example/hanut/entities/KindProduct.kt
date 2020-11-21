package com.example.hanut.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "KindProduct",
        foreignKeys = [
        ForeignKey(entity = Product::class, parentColumns = ["idProduct"], childColumns = ["idProduct"] )
        ]
    )
data class KindProduct (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idKindProduct")
    @NotNull
    val idKindProduct: Int,

    @ColumnInfo(name = "description")
    @NotNull
    val description: Int,

    @ColumnInfo(name = "idProduct")
    @NotNull
    val idProduct: Int

)