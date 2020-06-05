package com.mvvm.intelegenciamachinetest.data.source.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tasks")
data class WeatherResponse(
    val base: String?,
    @Embedded val clouds: Clouds?,
    @PrimaryKey
    val cod: Int?,
    @Embedded val coord: Coord?,
    val dt: Int?,
    @ColumnInfo(name = "id")
    val id: Int?,
    @Embedded val main: Main?,
    val name: String?,
    @Embedded val sys: Sys?,
    val visibility: Int?,
    @Embedded val wind: Wind?,

    @SerializedName("weather")
    var weather: MutableList<Weather>?
)

data class Clouds(
    val all: Int?
)

data class Coord(
    var lat: Double? = 0.0,
    val lon: Double? = 0.0
)

data class Main(
    val humidity: Int?,
    val pressure: Int?,
    val temp: Double?,
    val temp_max: Double?,
    val temp_min: Double?
)

data class Sys(
    val country: String?,
    @ColumnInfo(name = "ids")
    val id: Int?,
    val message: Double?,
    val sunrise: Int? = 0,
    val sunset: Int? = 0,
    val type: Int?
)

data class Weather(
    val description: String?,
    val icon: String?,
    @SerializedName("id")
    val idW: Int?,
    val main: String?
)

data class Wind(
    val deg: Int?,
    val speed: Double?
)