package com.example.myshop.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.myshop.R

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        fullScreen()

        @Suppress("DEPRECATION")
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()//should be closed - onDestroy
        }, 2000)
    }

}