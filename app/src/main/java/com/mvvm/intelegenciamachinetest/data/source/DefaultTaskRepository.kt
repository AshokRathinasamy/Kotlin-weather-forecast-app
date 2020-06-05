package com.mvvm.intelegenciamachinetest.data.source

import androidx.lifecycle.LiveData
import com.mvvm.intelegenciamachinetest.data.Result
import com.mvvm.intelegenciamachinetest.data.source.local.TaskDataSource
import com.mvvm.intelegenciamachinetest.data.source.model.WeatherResponse
import com.mvvm.intelegenciamachinetest.data.source.remote.ApiCall
import com.mvvm.intelegenciamachinetest.data.source.remote.SafeApiRequest
import org.json.JSONObject

class DefaultTaskRepository(
    private val apiCall: ApiCall,
    private val roomDb: TaskDataSource): SafeApiRequest(), TaskRepository {

    private val APPID = "5ad7218f2e11df834b0eaf3a33a39d2a"

    override suspend fun getAllData(lat : String, lon : String): Result<WeatherResponse> {
        var taskList = apiRequest { apiCall.getUserData(lat, lon, APPID) }
        if (taskList is Result.Success){
            saveTaskAll(taskList.data)
        }
        return roomDb.getTasks()
    }

    override fun observeTask(): LiveData<Result<WeatherResponse>> {
        return roomDb.observeTasks()
    }

    override suspend fun getTasks(): Result<WeatherResponse> {
        return roomDb.getTasks()
    }

    override suspend fun saveTaskAll(task: WeatherResponse) {
        return roomDb.saveTaskAll(task)
    }

    override suspend fun saveTask(task: WeatherResponse) {
        roomDb.saveTask(task)
    }
}