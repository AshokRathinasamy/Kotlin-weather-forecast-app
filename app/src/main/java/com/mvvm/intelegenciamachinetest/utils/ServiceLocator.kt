package com.mvvm.intelegenciamachinetest.utils

import android.content.Context
import androidx.room.Room
import com.mvvm.intelegenciamachinetest.data.source.DefaultTaskRepository
import com.mvvm.intelegenciamachinetest.data.source.local.TaskDataBase
import com.mvvm.intelegenciamachinetest.data.source.local.TaskDataSource
import com.mvvm.intelegenciamachinetest.data.source.local.TaskLocalDataSource
import com.mvvm.intelegenciamachinetest.data.source.remote.ApiCall

object ServiceLocator {

    private val apiCall = ApiCall()
    private var dataBase: TaskDataBase? = null
    @Volatile
    var taskRepository: DefaultTaskRepository? = null

    fun provideTasksRepository(context: Context): DefaultTaskRepository {
        synchronized(this){
            return taskRepository
                ?: createTasksRepository(
                    context
                )
        }
    }

    private fun createTasksRepository(context: Context):DefaultTaskRepository {
        val newRepo = DefaultTaskRepository(
            apiCall,
            createTaskLocalDataSource(
                context
            )
        )
        taskRepository = newRepo
        return newRepo
    }

    private fun createTaskLocalDataSource(context: Context): TaskDataSource {
        val database = dataBase
            ?: createDataBase(context)
        return TaskLocalDataSource(database.taskDao())
    }

    private fun createDataBase(context: Context) : TaskDataBase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            TaskDataBase::class.java,
            "Tasks.db"
        ).build()
        dataBase = result
        return result
    }
}