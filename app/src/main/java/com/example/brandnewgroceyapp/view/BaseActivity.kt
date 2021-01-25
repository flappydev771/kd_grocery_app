package com.example.brandnewgroceyapp.view

import android.app.ProgressDialog
import android.graphics.Paint
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import androidx.core.content.ContextCompat
import com.example.brandnewgroceyapp.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {


    fun addUnderLineToText(view:TextView){
        view.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }

fun showCustomDiaLog(dialog:ProgressDialog,title:String,msg:String){
    dialog.setMessage(msg)
    dialog.setTitle(title)
    dialog.show()

}
    fun hideCustomDialog(dialog:ProgressDialog){
        dialog.dismiss()
    }

    fun showSnackBar(state: Boolean, msg: String) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT)
        val view = snackBar.view
        if(state){
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            snackBar.show()
        }
        else{
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        }

    }
}