package com.yash.kagitam.db.plugins

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {
  @TypeConverter
  fun fromStringList(value: List<String>?) : String{
    return Gson().toJson(value)
  }

  @TypeConverter
  fun toStringList(value: String): List<String>{
    val typ = object : TypeToken<List<String>>() {}.type
    return Gson().fromJson(value, typ)
  }
}