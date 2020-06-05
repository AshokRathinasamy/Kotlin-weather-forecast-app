package com.mvvm.intelegenciamachinetest

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.mvvm.intelegenciamachinetest.data.source.TaskRepository
import com.mvvm.intelegenciamachinetest.utils.ServiceLocator

class IntelegenciaApplication : Application() {

    val taskRepository: TaskRepository
        get() = ServiceLocator.provideTasksRepository(this)

    fun checkInternet(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting() ?: false
    }
}