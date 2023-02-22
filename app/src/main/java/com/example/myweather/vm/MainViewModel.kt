package com.example.myweather.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweather.data.MainModel

class MainViewModel : ViewModel() {

    val currentLiveDataForHeadItem = MutableLiveData<MainModel>()

    val forecastLiveDataForListsItems = MutableLiveData<List<MainModel>>()
}