package com.example.myweather.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import com.example.myweather.data.MainModel

import com.example.myweather.databinding.ItemPagerBinding


class HoursDayAdapter : ListAdapter<MainModel, HoursDayAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pager, parent, false)
        return ItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position))
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemPagerBinding.bind(view)

        fun setData(model: MainModel) = with(binding) {
            textData.text = model.date
            textCondition.text = model.condition
            textInfo.text = model.tempCurrent

        }
    }

    class ItemComparator : DiffUtil.ItemCallback<MainModel>() {
        override fun areItemsTheSame(oldItem: MainModel, newItem: MainModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MainModel, newItem: MainModel): Boolean {
           return oldItem == newItem
        }

    }
}