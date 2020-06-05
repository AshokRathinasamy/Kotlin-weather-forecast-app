package com.mvvm.intelegenciamachinetest.dashboard

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mvvm.intelegenciamachinetest.data.source.TaskRepository

class DashViewModelFactory(val application: Application, private val repository: TaskRepository)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DashBoardViewModel(application, repository) as T
    }
}