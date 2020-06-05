package com.mvvm.intelegenciamachinetest.dashboard

import android.app.Application
import androidx.lifecycle.*
import com.mvvm.intelegenciamachinetest.IntelegenciaApplication
import com.mvvm.intelegenciamachinetest.data.Result
import com.mvvm.intelegenciamachinetest.data.source.TaskRepository
import com.mvvm.intelegenciamachinetest.data.source.model.WeatherResponse
import kotlinx.coroutines.launch

class DashBoardViewModel(private val context: Application, private val repository: TaskRepository) :
    ViewModel() {

    private val _realEstateList = repository.observeTask().switchMap {
        val listData = MutableLiveData<WeatherResponse>()
        when (it) {
            is Result.Success -> listData.value = it.data
//            else -> listData.value = WeatherResponse()
        }
        listData
    }
    val realEstateList: LiveData<WeatherResponse> = _realEstateList


    fun getWeatherReport(lat: String, lon: String) {
        if ((context as IntelegenciaApplication).checkInternet()) {
            viewModelScope.launch {
                val listData = MutableLiveData<WeatherResponse>()
                val result = repository.getTasks()
                when (result) {
                    is Result.Success -> listData.value = result.data
                }

                if (listData.value?.name == null || listData.value?.name.equals("")) {
                    repository.getAllData(lat, lon)
                }
            }
        }
    }

    fun getWeatherReportapi(lat: String, lon: String) {
        if ((context as IntelegenciaApplication).checkInternet()) {
            viewModelScope.launch {
                repository.getAllData(lat, lon)
            }
        }
    }

}