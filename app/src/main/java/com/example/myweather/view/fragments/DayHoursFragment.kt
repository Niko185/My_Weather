package com.example.myweather.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myweather.R

// TODO: Rename parameter arguments, choose names that match

class DayHoursFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__day_hours, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() = DayHoursFragment()

    }
}