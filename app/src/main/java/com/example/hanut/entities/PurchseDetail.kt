package com.example.hanut.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(
    tableName = "PurchseDetail",
    foreignKeys = [
        ForeignKey(entity = Purchase::class, parentColumns = ["idPurchase"], childColumns = ["idPurchase"]),
        ForeignKey(entity = Product::class, parentColumns = ["idProduct"], childColumns = ["idProduct"])
    ]
)
class PurchseDetail (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idPurchase")
    @NotNull
    val idPurchase: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idProduct")
    @NotNull
    val idProduct: Int
)