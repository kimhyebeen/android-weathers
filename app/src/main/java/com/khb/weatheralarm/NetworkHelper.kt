package com.khb.weatheralarm

import com.khb.weatheralarm.model.WeatherModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class NetworkHelper {
    lateinit var weatherAPI: WeatherAPI
    var retro: Retrofit? = null

    fun setRetrofit(retro: Retrofit?) {
        this.retro = retro
    }

    fun callWeatherList(map: Map<String, String>) {
        weatherAPI = retro!!.create(WeatherAPI::class.java)
        return weatherAPI.getWeatherList(map)?.enqueue(object : Callback<WeatherModel>{
            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                println("on failure")
            }

            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                println(response?.body().toString())
            }
        })
    }
}