package com.example.desafioandroid.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.desafioandroid.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        /** Cuida da animação. */
        val handle = Handler(this.mainLooper)
        handle.postDelayed({run { openActivity() }}, 4000)
    }

    private fun openActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}