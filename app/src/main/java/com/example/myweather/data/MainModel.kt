package com.example.myweather.data



data class MainModel(
    val nameCity: String,
    val timeDate: String,
    val condition: String,
    val imageCondition: String,
    val tempCurrent: String,
    val tempMax: String,
    val tempMin: String,
    val HoursCurrentDay: String
    )

