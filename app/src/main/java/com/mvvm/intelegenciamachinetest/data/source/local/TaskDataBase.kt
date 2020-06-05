package com.mvvm.intelegenciamachinetest.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mvvm.intelegenciamachinetest.data.source.model.WeatherResponse
import com.mvvm.intelegenciamachinetest.utils.TypeConvertersJSON

@Database(entities = [WeatherResponse::class], version = 1, exportSchema = false)
@TypeConverters(TypeConvertersJSON::class)
abstract class TaskDataBase : RoomDatabase() {
    abstract fun taskDao() : TaskDao
}