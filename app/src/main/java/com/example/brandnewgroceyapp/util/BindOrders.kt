package com.example.brandnewgroceyapp.util

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BindOrders {
    companion object{

        @SuppressLint("SetTextI18n")
        @BindingAdapter("setOrderDate")
        @JvmStatic
        fun bindOrderData(textView: TextView, date: String){
            textView.text = "Order Date: $date"
        }
        @SuppressLint("SetTextI18n")
        @BindingAdapter("setDeliveryDate")
        @JvmStatic
        fun bindDeliveryData(textView: TextView, date: String){
            textView.text = "Delivery Date: $date"
        }
        @SuppressLint("SetTextI18n")
        @BindingAdapter("setCost")
        @JvmStatic
        fun bindCost(textView: TextView, cost: String){
            textView.text = "Total Cost: $cost৳"
        }

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        @BindingAdapter("setChatTime")
        @JvmStatic
        fun bindChatTime(textView: TextView, stamp: String){
            val timestamp = Timestamp(stamp.toLong())
            val time = SimpleDateFormat("hh:mm a").format(Date(timestamp.time))
            textView.text =time
        }


    }
}