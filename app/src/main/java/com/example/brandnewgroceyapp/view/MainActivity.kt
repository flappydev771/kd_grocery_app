package com.example.brandnewgroceyapp.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.brandnewgroceyapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         setSupportActionBar(findViewById(R.id.mainToolBar))

        navController = findNavController(R.id.navHostFragment)

        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.orderFragment,
                R.id.chatFragment,
                R.id.accountFragment
            )
        )

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setupWithNavController(
            navController
        )
        setupActionBarWithNavController(navController, appBarConfiguration)


        //

    }





    fun setToolbar(){
        var toolbar: Toolbar = findViewById(R.id.mainToolBar)
        setSupportActionBar(toolbar)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() or super.onSupportNavigateUp()
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