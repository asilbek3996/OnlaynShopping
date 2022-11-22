package com.example.onlaynshop.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.onlaynshop.R
import com.example.onlineshop.screen.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        animationView.postDelayed({
            finish()
            startActivity(Intent(this,MainActivity::class.java))
        },3000)
    }
}