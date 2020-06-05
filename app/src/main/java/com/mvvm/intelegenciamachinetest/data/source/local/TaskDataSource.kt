package com.mvvm.intelegenciamachinetest.data.source.local

import androidx.lifecycle.LiveData
import com.mvvm.intelegenciamachinetest.data.Result
import com.mvvm.intelegenciamachinetest.data.source.model.WeatherResponse

interface TaskDataSource {
    fun observeTasks() : LiveData<Result<WeatherResponse>>

    suspend fun getTasks(): Result<WeatherResponse>

    suspend fun saveTaskAll(task: WeatherResponse)

    suspend fun saveTask(task: WeatherResponse)

}