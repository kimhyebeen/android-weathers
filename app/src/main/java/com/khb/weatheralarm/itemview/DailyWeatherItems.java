package com.khb.weatheralarm.itemview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.khb.weatheralarm.R;

public class DailyWeatherItems extends LinearLayout {
    public DailyWeatherItems(Context context) {
        super(context);

        init(context);
    }

    public DailyWeatherItems(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.item_daily_table, this, true);
    }
}
