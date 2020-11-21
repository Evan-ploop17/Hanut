package com.example.hanut.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(
    tableName = "PublicationSellDetail",
    foreignKeys =
        [
            ForeignKey(entity = PublicationToSell::class, parentColumns = ["idPublicationToSell"], childColumns = ["idPublicationToSell"]),
            ForeignKey(entity = Product::class, parentColumns = ["idProduct"], childColumns = ["idProduct"])
        ]
    )
data class PublicationSellDetail (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idPublicationToSell")
    @NotNull
    val idPublicationToSell: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idProduct")
    @NotNull
    val idProduct: Int
)