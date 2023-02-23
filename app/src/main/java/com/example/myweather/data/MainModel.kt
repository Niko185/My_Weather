package com.example.myweather.data



data class MainModel(
    val nameCity: String,
    val date: String,
    val condition: String,
    val imageCondition: String,
    val tempCurrent: String,
    val tempMax: String,
    val tempMin: String,
    val hoursCurrentDay: String
    )

