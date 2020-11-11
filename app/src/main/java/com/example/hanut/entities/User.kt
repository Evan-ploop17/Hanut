package com.example.hanut.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idUser")
    @NotNull
    val idUser: Int,

    @ColumnInfo(name = "nickName")
    @NotNull
    val nickName: String,

    @ColumnInfo(name = "name")
    @NotNull
    val name: String,

    @ColumnInfo(name = "lastName")
    @NotNull
    val lastName: String,

    @ColumnInfo(name = "address")
    @NotNull
    val address: String,

    @ColumnInfo(name = "postalCode")
    @NotNull
    val postalCode: Int,

    @ColumnInfo(name = "mail")
    @NotNull
    val mail: String,

    @ColumnInfo(name = "gender")
    @NotNull
    val gender: String
)