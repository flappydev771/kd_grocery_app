package com.example.brandnewgroceyapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.brandnewgroceyapp.R
import com.example.brandnewgroceyapp.model.ItemOrder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*


class SuccessOrderFragment : Fragment() {

    private val args: SuccessOrderFragmentArgs by navArgs()
    private lateinit var database: DatabaseReference
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Toast.makeText(requireContext(), args.totalPrice, Toast.LENGTH_SHORT).show()
        val view = inflater.inflate(R.layout.fragment_success_order, container, false)
        database = Firebase.database.reference
        view.findViewById<TextView>(R.id.showTotalText).text = "Total Price: "+args.totalPrice+"৳"
        val id = Firebase.auth.currentUser!!.uid
        creteOrderIntoDB(id)

        return view
    }

    @SuppressLint("SimpleDateFormat")
    private fun creteOrderIntoDB(id: String) {


        val date = SimpleDateFormat("dd.MM.yyyy").format(Date())
        val orderID = database.push().key.toString()
         val order = ItemOrder(orderID,date,"Cash on Delivery",date,args.totalPrice)

        database.child("order").child(id).child(orderID).setValue(order).addOnCompleteListener { task->
            if(task.isSuccessful){
                Toasty.success(requireContext(),"Please check your order",Toasty.LENGTH_SHORT).show()
            }
            else{
                Toasty.error(requireContext(),"Order Placement failed...",Toasty.LENGTH_SHORT).show()

            }

        }

    }

}