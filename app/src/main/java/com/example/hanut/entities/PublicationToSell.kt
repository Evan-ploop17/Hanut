package com.example.hanut.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity(tableName = "PublicationToSell",
    // necesita saber a que entidad pertenece, como se llama en esa entidad y como se llamará en esta
    foreignKeys = [ ForeignKey(entity = User::class, parentColumns = ["idUser"], childColumns = ["idUser"])
    ]
)
data class PublicationToSell (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idPublicationToSell")
    @NotNull
    val idPublicationToSell: Int,

    //Foreign Key
    @ColumnInfo(name = "idUser")
    @NotNull
    val idUser: Int
)