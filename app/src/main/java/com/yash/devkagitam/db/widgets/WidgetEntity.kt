package com.yash.devkagitam.db.widgets

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

const val WIDGET_DB = "widgets_db"

@Entity(tableName = WIDGET_DB)
data class WidgetEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "owner") val owner: String,
    @ColumnInfo(name = "widgetClass") val widgetClass: String,
    @ColumnInfo(name = "apk_path") val apkPath: String,
    @ColumnInfo(name = "selected") val selected: Boolean
)
