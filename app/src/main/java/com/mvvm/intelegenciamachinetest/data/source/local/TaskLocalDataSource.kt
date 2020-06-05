package com.mvvm.intelegenciamachinetest.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mvvm.intelegenciamachinetest.data.Result
import com.mvvm.intelegenciamachinetest.data.source.model.WeatherResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class TaskLocalDataSource internal constructor(
    private val taskDao: TaskDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskDataSource {

    override fun observeTasks(): LiveData<Result<WeatherResponse>> {
        return taskDao.observeTask().map {
            Result.Success(it)
        }
    }

    override suspend fun getTasks(): Result<WeatherResponse> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(taskDao.getTask())
        } catch (e: Exception) {
            Result.Error("DataBase Error", e.message!!)
        }
    }

    override suspend fun saveTaskAll(task: WeatherResponse) {
        taskDao.saveAllTask(task)
    }

    override suspend fun saveTask(task: WeatherResponse) {
        taskDao.insertTask(task)
    }
}