package com.mvvm.intelegenciamachinetest.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mvvm.intelegenciamachinetest.data.source.model.WeatherResponse

@Dao
interface TaskDao {

    @Query("Select * From Tasks")
    fun observeTask(): LiveData<WeatherResponse>

    @Query("Select * From Tasks")
    suspend fun getTask(): WeatherResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task : WeatherResponse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllTask(task: WeatherResponse)

}