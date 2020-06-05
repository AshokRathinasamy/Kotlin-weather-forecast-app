package com.mvvm.intelegenciamachinetest.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mvvm.intelegenciamachinetest.data.source.model.Weather

object TypeConvertersJSON {
        @TypeConverter
        @JvmStatic
        fun fromString(value: String?): MutableList<Weather>? {
            val type = object : TypeToken<List<Weather>>() {}.type
            return Gson().fromJson(value, type)
        }
        @TypeConverter
        @JvmStatic
        fun fromArrayList(list: MutableList<Weather>?): String? {
            val type = object : TypeToken<List<Weather>>() {}.type
            return Gson().toJson(list, type)
        }
}