package com.yash.kagitam.db.plugins

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "MetaDataPlugin")
data class MetaDataPluginEntity(
    @PrimaryKey val name: String,
    val author: String,
    val version: String,
    val id: String = UUID.randomUUID().toString(),
    val entryPoint: String,
    val widgets: List<String>,
    val apiClass: String?,
    val path: String
)

