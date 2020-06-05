package com.mvvm.intelegenciamachinetest.data.source

import androidx.lifecycle.LiveData
import com.mvvm.intelegenciamachinetest.data.Result
import com.mvvm.intelegenciamachinetest.data.source.model.WeatherResponse

interface TaskRepository {

    //API Implementation
    suspend fun getAllData(lat : String, lon : String): Result<WeatherResponse>

    //Room Implementation
    fun observeTask(): LiveData<Result<WeatherResponse>>

    suspend fun getTasks(): Result<WeatherResponse>

    suspend fun saveTaskAll(task: WeatherResponse)

    suspend fun saveTask(task: WeatherResponse)

}