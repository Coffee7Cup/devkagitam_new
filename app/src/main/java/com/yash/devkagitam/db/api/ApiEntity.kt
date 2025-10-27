package com.yash.devkagitam.db.api

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity("api_db")
data class ApiEntity (
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val owner : String,
    val apiClass : String,
    val apkPath : String
)
