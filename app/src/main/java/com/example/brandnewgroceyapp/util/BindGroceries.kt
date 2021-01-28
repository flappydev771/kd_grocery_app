package com.example.brandnewgroceyapp.util

import android.annotation.SuppressLint
import android.graphics.Paint
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.example.brandnewgroceyapp.R
import de.hdodenhof.circleimageview.CircleImageView


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

        @BindingAdapter("setCircleImage")
        @JvmStatic
        fun bindCircleImages(imageView: CircleImageView,url:String){
            if(url.isEmpty()){
                Glide.with(imageView.context).load(url)
                    .placeholder(ContextCompat.getDrawable(imageView.context, R.drawable.face))
                    .into(imageView)
            }
            else{
                Glide.with(imageView.context).load(url)
                    .placeholder(getProgressDrawable(imageView))
                    .into(imageView)
            }

        }

        @BindingAdapter("setCircleImageForOther")
        @JvmStatic
        fun bindCircleImagesForOther(imageView: CircleImageView,url:String){
            if(url.isEmpty()){
                Glide.with(imageView.context).load(url)
                    .placeholder(ContextCompat.getDrawable(imageView.context, R.drawable.boy_face))
                    .into(imageView)
            }
            else{

                Glide.with(imageView.context).load(url)
                    .placeholder(getProgressDrawable(imageView))
                    .into(imageView)
            }

        }

        @BindingAdapter("setCircleImageWithCoil")
        @JvmStatic
        fun bindCircleImagesWithCoil(imageView: CircleImageView,url:String){

           imageView.load(url) {
               crossfade(true)
               placeholder(getProgressDrawable(imageView))
               transformations(CircleCropTransformation())
           }

        }


    }
}