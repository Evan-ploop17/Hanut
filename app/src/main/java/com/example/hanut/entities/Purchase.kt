package com.example.hanut.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(
    tableName = "Purchase",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["idUser"], childColumns = ["idUser"]),
        ForeignKey(entity = PublicationToSell::class, parentColumns = ["PublicationToSell"], childColumns = ["idPublication"])
    ]
    )
data class Purchase(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idPurchase")
    @NotNull
    val idPurchase: Int,

    //Foreign Key
    @ColumnInfo(name = "idUser")
    @NotNull
    val idUser: Int,

    //Foreign Key
    @ColumnInfo(name = "idPublication")
    @NotNull
    val idPublication: Int

)