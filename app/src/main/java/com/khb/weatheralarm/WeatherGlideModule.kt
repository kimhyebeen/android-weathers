package com.khb.weatheralarm

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class WeatherGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {

    }
}