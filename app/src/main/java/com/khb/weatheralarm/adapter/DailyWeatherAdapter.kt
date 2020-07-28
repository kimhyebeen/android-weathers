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
import com.khb.weatheralarm.model.DailyTableItem
import kotlinx.android.synthetic.main.item_daily_table.view.*

class DailyWeatherAdapter : RecyclerView.Adapter<DailyWeatherAdapter.ItemViewHolder>() {
    var itemList = ArrayList<DailyTableItem>()
    lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DailyWeatherAdapter.ItemViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_daily_table, parent, false)
        this.context = parent.context

        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DailyWeatherAdapter.ItemViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun addItem(items: DailyTableItem) {
        itemList.add(items)
        notifyDataSetChanged()
    }

    fun removeItem(i: Int) {
        itemList.removeAt(i)
        notifyItemRemoved(i)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView = itemView.dailyDateTextView
        val dailyWeatherImage = itemView.dailyWeatherImageView
        val dailyMaxTempTextView = itemView.dailyMaxTempTextView
        val dailyMinTempTextView = itemView.dailyMinTempTextView

        fun onBind(i: Int) {
            dateTextView.text = itemList[i].date
            Glide.with(context)
                .load(itemList[i].imageUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
                .into(dailyWeatherImage)
            dailyMaxTempTextView.text = itemList[i].maxTemp
            dailyMinTempTextView.text = itemList[i].minTemp
        }
    }

}