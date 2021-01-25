package com.example.brandnewgroceyapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.brandnewgroceyapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delaySplash()
        }

    }

    suspend fun delaySplash(){
        delay(2000)
        startActivity(Intent(applicationContext,SelectionActivity::class.java))

    Animatoo.animateSwipeLeft(this)
    finishAffinity()
    }
}