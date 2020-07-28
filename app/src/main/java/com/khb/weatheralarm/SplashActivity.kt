package com.khb.weatheralarm

import android.content.Intent
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    var handler = Handler()

    override fun onResume() {
        super.onResume()
        /* 1.3초 동안 splash 화면 띄우기 */
        handler.postDelayed({
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1300)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var size = Point()
        windowManager.defaultDisplay.getRealSize(size)
        splashLogoImageView.layoutParams.height = size.y / 5 * 3
    }
}