package com.example.myweather.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweather.data.MainModel
import com.example.myweather.databinding.FragmentDayHoursBinding
import com.example.myweather.view.adapters.HoursDayAdapter
import com.example.myweather.vm.MainViewModel
import org.json.JSONArray
import org.json.JSONObject

class DayHoursFragment : Fragment() {
    private lateinit var binding: FragmentDayHoursBinding
    private lateinit var myAdapter: HoursDayAdapter
    private val mainViewModel: MainViewModel by activityViewModels()

    // Base Functions
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDayHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observerMainViewModelAndAdapterRecyclerView()
    }

    // Recycler View Functions

    // Initialization rcView and his adapter
    private fun initRecyclerView() = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        myAdapter = HoursDayAdapter()
        recyclerView.adapter = myAdapter

    }

    private fun observerMainViewModelAndAdapterRecyclerView() {
    mainViewModel.currentLiveDataForHeadItem.observe(viewLifecycleOwner) {  
        myAdapter.submitList(getHoursListExtractArray(it))
        }
    }

    // Форматруем String в Json чтобы можно было доставать данные из Json.(Достаем инфу по часам)
    private fun getHoursListExtractArray(model: MainModel): List<MainModel> {
        val hoursArrayJson = JSONArray(model.hoursCurrentDay)
        val hoursListExtractArray = ArrayList<MainModel>()


        for(index in 0 until hoursArrayJson.length()) {
            val hoursModel = MainModel(
                "",
                (hoursArrayJson[index] as JSONObject).getString("time"),
                (hoursArrayJson[index] as JSONObject).getJSONObject("condition").getString("text"),
                (hoursArrayJson[index] as JSONObject).getJSONObject("condition").getString("icon"),
                (hoursArrayJson[index] as JSONObject).getString("temp_c"),
                "" ,
                "",
                ""
            )
            hoursListExtractArray.add(hoursModel)
        }
        return hoursListExtractArray
    }

    companion object {

        @JvmStatic
        fun newInstance() = DayHoursFragment()

    }
}