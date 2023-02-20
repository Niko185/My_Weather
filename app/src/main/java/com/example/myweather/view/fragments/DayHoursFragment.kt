package com.example.myweather.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweather.data.MainModel
import com.example.myweather.databinding.FragmentDayHoursBinding
import com.example.myweather.view.adapters.HoursDayAdapter

class DayHoursFragment : Fragment() {
    private lateinit var binding: FragmentDayHoursBinding
    private lateinit var myAdapter: HoursDayAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDayHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observer()
    }
    private fun init() = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        myAdapter = HoursDayAdapter()
        recyclerView.adapter = myAdapter

    }

    private fun observer() {

        val listModel = listOf(
            MainModel(
                "",
                "15.05.2023",
                "Sunny",
                "",
                "10°",
                "",
                "",
                "10:00"
            ),
            MainModel(
                "",
                "15.05.2023",
                "Sunny",
                "",
                "15°",
                "",
                "",
                "15:00",
            ),
            MainModel(
                "",
                "15.05.2023",
                "Rain",
                "",
                "20°",
                "",
                "",
                "20:00"
        )

        )
        myAdapter.submitList(listModel)
    }

    companion object {

        @JvmStatic
        fun newInstance() = DayHoursFragment()

    }
}