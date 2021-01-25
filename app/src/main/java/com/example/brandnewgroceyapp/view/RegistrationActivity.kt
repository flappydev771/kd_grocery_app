package com.example.brandnewgroceyapp.view

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.brandnewgroceyapp.R
import com.example.brandnewgroceyapp.databinding.ActivityRegistrationBinding
import com.example.brandnewgroceyapp.model.Customer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegistrationActivity : BaseActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration)
        setToolBar()
        database = Firebase.database.reference
        mAuth = Firebase.auth
        progressDialog = ProgressDialog(this)

        addUnderLineToText(binding.logTextId)



    }

    private fun setToolBar() {
        setSupportActionBar(binding.regToolbar)
        supportActionBar!!.setTitle("Registration")
        binding.regToolbar.let {

            it.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
            it.setNavigationIcon(R.drawable.ic_arrow)
            it.setNavigationOnClickListener {
                startActivity(Intent(applicationContext, SelectionActivity::class.java))
                Animatoo.animateSwipeRight(this)
                finishAffinity()
            }
        }


    }

    fun regButtonClicked(view: View) {

        validateRegInfo()

    }

    private fun validateRegInfo() {

        if (binding.regNameId.text.toString().trim().isEmpty()) {
            binding.regNameId.setError("Name is empty")
            binding.regNameId.requestFocus()

        } else if (binding.regEmailID.text.toString().trim().isEmpty()) {
            binding.regEmailID.setError("Name is empty")
            binding.regEmailID.requestFocus()

        } else if (binding.regPassID.text.toString().trim().isEmpty()) {
            binding.regPassID.setError("Name is empty")
            binding.regPassID.requestFocus()

        } else {
            showCustomDiaLog(progressDialog, "User Registration", "Please Wait...")
            register()
        }
    }


    private fun register() {
        Toast.makeText(this, "work", Toast.LENGTH_SHORT).show()

        val name = binding.regNameId.text.toString().trim();
        val email = binding.regEmailID.text.toString().trim();
        val password = binding.regPassID.text.toString().trim();


        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storeIntoData(name, email, password)
                } else {
                    hideCustomDialog(progressDialog)
                    showSnackBar(false, "Registration failed... ")

                }

            }

    }

    private fun storeIntoData(name: String, email: String, password: String) {

        val id = Firebase.auth.currentUser!!.uid
        val customer = Customer(id,name, email, password,"customer")

        database.child("user").child(id).setValue(customer).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                hideCustomDialog(progressDialog)
                showSnackBar(true, "Registration Complete...")
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finishAffinity()
            } else {
                hideCustomDialog(progressDialog)
                showSnackBar(false, "Data writing failed...")

            }
        }
    }

    fun onLogTextClicked(view: View) {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        Animatoo.animateSwipeLeft(this)
        finishAffinity()
    }
}
