package com.masykur.githubuser2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.masykur.githubuser2.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

       supportActionBar?.hide()
        Handler().postDelayed({
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))

            finish()
        },4000)
    }
    }
