package com.example.brandnewgroceyapp.util

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.github.marlonlom.utilities.timeago.TimeAgo
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
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        @BindingAdapter("setChatTimeAgo")
        @JvmStatic
        fun bindChatTimeAgo(textView: TextView, stamp: String){

            var time:String = TimeAgo.using(stamp.toLong())
            textView.text = time
        }

        @SuppressLint("SetTextI18n")
        @BindingAdapter("setAddress")
        @JvmStatic
        fun bindAddress(textView: TextView, address: String){
            textView.text = "Address: $address"
        }
        @SuppressLint("SetTextI18n")
        @BindingAdapter("setPhone")
        @JvmStatic
        fun bindPhone(textView: TextView, phone: String){
            textView.text = "Phone no: $phone"
        }
        @SuppressLint("SetTextI18n")
        @BindingAdapter("setName")
        @JvmStatic
        fun bindName(textView: TextView, name: String){
            textView.text = "Customer Name: $name"
        }

    }
}