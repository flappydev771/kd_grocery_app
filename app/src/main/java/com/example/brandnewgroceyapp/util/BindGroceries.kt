package com.example.brandnewgroceyapp.util

import android.annotation.SuppressLint
import android.graphics.Paint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide



class BindGroceries {

    companion object{

        fun getProgressDrawable(imageView: ImageView):CircularProgressDrawable{
            var progressDrawable:CircularProgressDrawable = CircularProgressDrawable(imageView.context)
            progressDrawable.centerRadius = 50f
            progressDrawable.strokeWidth = 10f
            progressDrawable.start()
            return progressDrawable
        }

        @SuppressLint("SetTextI18n")
        @BindingAdapter("setPrice1")
        @JvmStatic
        fun bindPrice1(textView: TextView,price1:String){
            textView.text = "$price1৳"
            textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        @SuppressLint("SetTextI18n")
        @BindingAdapter("setPrice2")
        @JvmStatic
        fun bindPrice2(textView: TextView,price2:String){
            textView.text = "$price2৳"

        }

        @BindingAdapter("setImage")
        @JvmStatic
        fun bindImages(imageView: ImageView,url:String){
          Glide.with(imageView.context).load(url)
              .placeholder(getProgressDrawable(imageView))
              .into(imageView)
        }





    }
}