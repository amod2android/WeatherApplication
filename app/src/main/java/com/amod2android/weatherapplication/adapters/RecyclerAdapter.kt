package com.amod2android.weatherapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amod2android.weatherapplication.R
import com.amod2android.weatherapplication.RecyclerData
import kotlinx.android.synthetic.main.item_forecast.view.*


class RecyclerAdapter(val list: List<RecyclerData>) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_forecast, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list.get(position))
    }



    override fun getItemCount() = list.size

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTimeOfDay = view.textViewTimeOfDay
        val textViewDayOfWeek = view.textViewDayOfWeek
        val textViewTemp = view.textViewTemp
        val textViewMinTemp = view.textViewMinTemp
        val textViewMaxTemp = view.textViewMaxTemp

        fun bind(data: RecyclerData) {
            textViewTimeOfDay.text = data.time
            textViewDayOfWeek.text = data.day
            textViewTemp.text = data.temp
            textViewMinTemp.text = data.minTemp
            textViewMaxTemp.text = data.maxTemp

        }
    }

}