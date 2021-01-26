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


class PaymentFragment : Fragment() {

    val arg: PaymentFragmentArgs by navArgs()
    private lateinit var binding:FragmentPaymentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        //Toast.makeText(requireContext(),arg.totalPrice,Toast.LENGTH_SHORT).show()

        binding.nextButtonID.setOnClickListener {
            val action = PaymentFragmentDirections.actionPaymentFragmentToSuccessOrderFragment(arg.totalPrice)
            findNavController().navigate(action)
        }

        return binding.root
    }


}