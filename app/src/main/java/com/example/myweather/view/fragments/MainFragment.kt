package com.example.myweather.view.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myweather.R
import com.example.myweather.databinding.FragmentMainBinding
import com.example.myweather.utils.isPermissionGranted
import com.example.myweather.view.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
const val API_KEY = "99227bc267bb4ce8a9080001231402"

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private val fragmentList = listOf<Fragment>(
        DayHoursFragment.newInstance(),
        NextDaysFragment.newInstance()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkedUserPermissionsInList()
        initViewPager()
        requestWeatherApi("London")
    }

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
            { result -> Log.d("MyLog", "result MainRequest: $result") },
            { error -> Log.d("MyLog", "error MainRequest: $error") }
        )
        queue.add(mainRequest)
    }

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


    private fun checkedAnswerUserPermissionsDialog() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        Toast.makeText(activity, "User Answer - $it - true or false if(true) // code else //code", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkedUserPermissionsInList() {
        if(!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            checkedAnswerUserPermissionsDialog()
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    companion object {
        @JvmStatic
        fun fragInstance() = MainFragment()
    }
}