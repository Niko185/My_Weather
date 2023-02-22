package com.example.myweather.view.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myweather.R
import com.example.myweather.data.MainModel
import com.example.myweather.databinding.FragmentMainBinding
import com.example.myweather.utils.isPermissionGranted
import com.example.myweather.view.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import org.json.JSONObject

const val API_KEY = "99227bc267bb4ce8a9080001231402"

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private val fragmentList = listOf<Fragment>(
        DayHoursFragment.newInstance(),
        NextDaysFragment.newInstance()
    )

    // Override Functions
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPresencePermissionUser()
        initViewPager()
        requestWeatherApi("London")
    }

    // API Functions
    private fun requestWeatherApi(city: String) {
        val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
                API_KEY +
                "&q=" +
                city +
                "&days=" +
                "3" +
                "&aqi=no&alerts=no"

        val queue = Volley.newRequestQueue(context)
        val mainRequest = StringRequest(
            Request.Method.GET,
            url,
            { response -> parsingApi(response) },
            { error -> Log.d("MyLog", "error MainRequest: $error") }
        )
        queue.add(mainRequest)
    }

    private fun parsingApi(response: String) {
        val fullJsonObject = JSONObject(response)

        val daysModelListForecast  = getForecastDaysModel(fullJsonObject)

         getModelHeadItem(fullJsonObject, daysModelListForecast[0])


    }

    // dayModel from daysModelListForecast[0] - [0] position == currentDay.
    private fun getModelHeadItem(fullJsonObject: JSONObject, dayModel: MainModel) {

        val headModel = MainModel(
            fullJsonObject.getJSONObject("location").getString("name"),
            fullJsonObject.getJSONObject("current").getString("last_updated"),
            fullJsonObject.getJSONObject("current").getJSONObject("condition").getString("text"),
            fullJsonObject.getJSONObject("current").getJSONObject("condition").getString("icon"),
            fullJsonObject.getJSONObject("current").getString("temp_c"),
            dayModel.tempMax,
            dayModel.tempMin,
            dayModel.hoursCurrentDay
        )
        Log.d("Mylog", "TempMin: ${headModel.tempMin}")
    }


    private fun getForecastDaysModel(fullJsonObject: JSONObject): List<MainModel> {
        val dayModelList = ArrayList<MainModel>()


        val arrayForecastDays = fullJsonObject.getJSONObject("forecast").getJSONArray("forecastday")
        for(index in 0 until arrayForecastDays.length()) {

            // One Forecast Day from Array - in "oneDayJsonObject" - является объектом на уровне представления Json.
            val oneDayJsonObject = arrayForecastDays[index] as JSONObject


            val dayModel = MainModel(
                fullJsonObject.getJSONObject("location").getString("name"),
                oneDayJsonObject.getString("date"),
                oneDayJsonObject.getJSONObject("day").getJSONObject("condition").getString("text"),
                oneDayJsonObject.getJSONObject("day").getJSONObject("condition").getString("icon"),
                "",
                oneDayJsonObject.getJSONObject("day").getString("maxtemp_c"),
                oneDayJsonObject.getJSONObject("day").getString("mintemp_c"),
                oneDayJsonObject.getJSONArray("hour").toString()
                )
            dayModelList.add(dayModel)
        }
        return dayModelList
    }


    // ViewPager Functions
    private fun initViewPager() {
        var viewPagerAdapter = ViewPagerAdapter(activity as AppCompatActivity, fragmentList)
            binding.viewPager.adapter = viewPagerAdapter


         val tabItemList = listOf(
            getString(R.string.tab_item_day_hours),
            getString(R.string.tab_item_next_days)
        )

        TabLayoutMediator(binding.tabLayout, binding.viewPager) {
            tabItem, position -> tabItem.text = tabItemList[position]
        }.attach()

    }


    // Permissions Functions
    private fun checkPresencePermissionUser() {
        if(!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            checkResponseUserPermissionsDialog()
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun checkResponseUserPermissionsDialog() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        Log.d("MyLog", "User Answer this: $it")
        }
    }

    // Instance Fragment
    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }



    private fun test(fullJsonObject: JSONObject){
        val arrayForecastDays = fullJsonObject.getJSONObject("forecast").getJSONArray("forecastday")
        Log.d("MyLog", "this: $arrayForecastDays")
    }
}