package com.example.brandnewgroceyapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.example.brandnewgroceyapp.R
import com.example.brandnewgroceyapp.databinding.FragmentPaymentBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import es.dmoral.toasty.Toasty


class PaymentFragment : Fragment() {

    val arg: PaymentFragmentArgs by navArgs()
    private lateinit var binding:FragmentPaymentBinding
    private val id = Firebase.auth.currentUser!!.uid
    private lateinit var database:DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        database = Firebase.database.reference
        //Toast.makeText(requireContext(),arg.totalPrice,Toast.LENGTH_SHORT).show()

        binding.nextButtonID.setOnClickListener {
           checkProfile(id)
        }




        return binding.root
    }
    private fun checkProfile(id: String) {
        database.child("profile")
            .child(id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val action = PaymentFragmentDirections.actionPaymentFragmentToSuccessOrderFragment(arg.totalPrice)
                        findNavController().navigate(action)
                    }
                    else{
                        Toasty.error(requireContext(), "You need to create profile to place order...", Toasty.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


}