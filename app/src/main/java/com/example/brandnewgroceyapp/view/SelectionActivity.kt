package com.example.brandnewgroceyapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.brandnewgroceyapp.R
import com.google.android.material.button.MaterialButton

class SelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

    }

    fun regButtonPressed(view: View) {
        startActivity(Intent(applicationContext,RegistrationActivity::class.java))
        Animatoo.animateSwipeLeft(this)

    }
    fun logButtonPressed(view: View) {
        startActivity(Intent(applicationContext,LoginActivity::class.java))
        Animatoo.animateSwipeLeft(this)


    }
}