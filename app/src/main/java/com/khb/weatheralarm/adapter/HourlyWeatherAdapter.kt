package com.khb.weatheralarm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.khb.weatheralarm.R
import com.khb.weatheralarm.table_model.HourlyTable
import kotlinx.android.synthetic.main.item_hourly_table.view.*

class HourlyWeatherAdapter : RecyclerView.Adapter<HourlyWeatherAdapter.ItemViewHolder>() {
    var itemList = ArrayList<HourlyTable>()
    lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HourlyWeatherAdapter.ItemViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_hourly_table, parent, false)
        this.context = parent.context

        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HourlyWeatherAdapter.ItemViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun addItem(items: HourlyTable) {
        itemList.add(items)
        notifyDataSetChanged()
    }

    fun removeItem(i: Int) {
        itemList.removeAt(i)
        notifyItemRemoved(i)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView = itemView.hourlyItemTimeTextView
        val hourlyWeatherImage = itemView.hourlyItemImageView
        val hourlyTempTextView = itemView.hourlyItemTempTextView

        fun onBind(i: Int) {
            timeTextView.text = itemList[i].time
            Glide.with(context)
                .load(itemList[i].imageUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
                .into(hourlyWeatherImage)
            hourlyTempTextView.text = itemList[i].temp
        }
    }

}