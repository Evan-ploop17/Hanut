package com.example.hanut.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "KindProduct")
data class KindProduct (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idKindProduct")
    @NotNull
    val idKindProduct: Int,

    @ColumnInfo(name = "description")
    @NotNull
    val description: Int

)