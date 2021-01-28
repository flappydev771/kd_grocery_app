package com.example.brandnewgroceyapp.view

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.brandnewgroceyapp.R
import com.example.brandnewgroceyapp.databinding.ActivityLoginBinding
import com.example.brandnewgroceyapp.util.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mAuth: FirebaseAuth

    @Inject lateinit var sharedPreferences:SharedPreferences
    @Inject lateinit var editor:SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setToolBar()
        progressDialog = ProgressDialog(this)

        mAuth = Firebase.auth
        addUnderLineToText(binding.regTextId)

    }

    override fun onStart() {
        super.onStart()
        binding.logEmailID.setText(sharedPreferences.getString(Util.USER_EMAIL, ""))
        binding.logPassID.setText(sharedPreferences.getString(Util.USER_PASS, ""))

    }

    private fun setToolBar() {
        setSupportActionBar(binding.logToolbar)
        supportActionBar!!.setTitle("Login")
        binding.logToolbar.let {


            it.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
            it.setNavigationIcon(R.drawable.ic_arrow)
            it.setNavigationOnClickListener {
                startActivity(Intent(applicationContext, SelectionActivity::class.java))
                Animatoo.animateSwipeRight(this)
                finishAffinity()
            }
        }


    }

    private fun validateRegInfo() {

        if (binding.logEmailID.text.toString().trim().isEmpty()) {
            binding.logEmailID.setError("Email is empty")
            binding.logEmailID.requestFocus()


        } else if (binding.logPassID.text.toString().trim().isEmpty()) {
            binding.logPassID.setError("Password is empty")
            binding.logPassID.requestFocus()

        } else {
            showCustomDiaLog(progressDialog, "User Login", "Please Wait...")
            login()
        }
    }

    private fun login() {
        val email = binding.logEmailID.text.toString().trim();
        val password = binding.logPassID.text.toString().trim();

        if (binding.rememberMe.isChecked) {

            editor.putString(Util.USER_EMAIL, email)
            editor.putString(Util.USER_PASS, password)
            editor.apply()

        }



        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                hideCustomDialog(progressDialog)
                showSnackBar(true, "Successfully Logged in...")
                startActivity(Intent(this, MainActivity::class.java))
                Animatoo.animateSwipeLeft(this)
                finishAffinity()
            } else {
                hideCustomDialog(progressDialog)
                showSnackBar(false, " Login Failed...")

            }
        }


    }

    fun logButtonPressed(view: View) {
        validateRegInfo()
    }

    fun regTextPressed(view: View) {
        startActivity(Intent(this, RegistrationActivity::class.java))
        Animatoo.animateSwipeLeft(this)
        finishAffinity()

    }

    fun forgetPassword(view: View) {}
}