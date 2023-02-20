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
            { result -> extractApiInstance(result) },
            { error -> Log.d("MyLog", "error MainRequest: $error") }
        )
        queue.add(mainRequest)
    }

    private fun extractApiInstance(result: String) {
        val mainJsonObject = JSONObject(result)

        val completedForecastList = getForecastDaysModel(mainJsonObject)

        formModelDataForHead(mainJsonObject, completedForecastList[0])
    }


    private fun formModelDataForHead(mainJsonObjectInstance: JSONObject, completedForecastList: MainModel) {

        val headModel = MainModel(
            mainJsonObjectInstance.getJSONObject("location").getString("name"),
            mainJsonObjectInstance.getJSONObject("current").getString("last_updated"),
            mainJsonObjectInstance.getJSONObject("current").getJSONObject("condition").getString("text"),
            mainJsonObjectInstance.getJSONObject("current").getJSONObject("condition").getString("icon"),
            mainJsonObjectInstance.getJSONObject("current").getString("temp_c"),
            completedForecastList.tempMax,
            completedForecastList.tempMin,
            completedForecastList.HoursCurrentDay
        )
        Log.d("Mylog", "TempMin: ${headModel.tempMin}")
    }

    private fun getForecastDaysModel(mainJsonObjectInstance: JSONObject): List<MainModel> {
        val forecastModelList = ArrayList<MainModel>()

        // Массив в котором хранятся данные о прогнозе дней по индексу.
        val arrayForecastDays = mainJsonObjectInstance.getJSONObject("forecast").getJSONArray("forecastday")
        for(index in 0 until arrayForecastDays.length()) {
            val dayJsonObjectInstance = arrayForecastDays[index] as JSONObject

            val forecastModel = MainModel(
                mainJsonObjectInstance.getJSONObject("location").getString("name"),
                dayJsonObjectInstance.getString("date"),
                dayJsonObjectInstance.getJSONObject("day").getJSONObject("condition").getString("text"),
                dayJsonObjectInstance.getJSONObject("day").getJSONObject("condition").getString("icon"),
                "",
                dayJsonObjectInstance.getJSONObject("day").getString("maxtemp_c"),
                dayJsonObjectInstance.getJSONObject("day").getString("mintemp_c"),
                dayJsonObjectInstance.getJSONArray("hour").toString()
                )
            forecastModelList.add(forecastModel)
        }
        return forecastModelList
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
}