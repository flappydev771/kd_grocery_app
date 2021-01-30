package com.example.brandnewgroceyapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
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

    fun showCustomDialog(){

        val alert = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.custom_dialog_layout, null)
        val yes = view.findViewById<Button>(R.id.yes_button_id)
        val no = view.findViewById<Button>(R.id.no_button_id)
        alert.setView(view)

        val dialog = alert.create()

        yes.setOnClickListener {

            finishAndRemoveTask()
        }
        no.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    override fun onBackPressed() {
        showCustomDialog()
    }
}