package com.example.hanut.dao

import androidx.room.Insert
import com.example.hanut.entities.User

interface UserDAO {

    @Insert
    fun insert(user: User)


}