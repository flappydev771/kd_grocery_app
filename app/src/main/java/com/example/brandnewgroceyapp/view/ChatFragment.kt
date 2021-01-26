package com.example.brandnewgroceyapp.view

import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.brandnewgroceyapp.R
import com.example.brandnewgroceyapp.adapter.ChatUserAdapter

import com.example.brandnewgroceyapp.databinding.FragmentChatBinding
import com.example.brandnewgroceyapp.model.Customer
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.LandingAnimator
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : Fragment() {



    private lateinit var binding: FragmentChatBinding
    private lateinit var database:DatabaseReference

    private var seller:Boolean = false
    private val adapter:ChatUserAdapter by lazy { ChatUserAdapter()}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        database = Firebase.database.reference
        binding = FragmentChatBinding.inflate(inflater, container, false)

        setRecyclerView()
        ifSeller()


        return binding.root
    }

    fun ifSeller(){
        val userId = Firebase.auth.currentUser!!.uid
         database.child("seller").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               for(id in snapshot.children){
                   if(userId == id.value.toString())
                   {
                       seller = true


                   }
                   Log.e("id: ",id.value.toString())
               }
                if(seller){

                        getAllUsersFromFireDB()


                    //Toast.makeText(requireContext(),"seller",Toast.LENGTH_SHORT).show()
                }
                else{
                    getAllSellersFromFireDB()

                    //Toast.makeText(requireContext(),"not seller",Toast.LENGTH_SHORT).show()

                }
            }

            override fun onCancelled(error: DatabaseError) {Log.e("Error: ",error.message)}

        })


    }
  fun getAllUsersFromFireDB(){
        val customers:MutableList<Customer> = ArrayList()
        val db = Firebase.database.reference.child("user")
        db.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(user in snapshot.children){
                    val customer = user.getValue(Customer::class.java)

                    if(customer!!.status=="customer")
                    {
                        Log.e("name",customer.status)
                        customers.add(customer)

                    }

                }
                adapter.setData(customers)
            }

            override fun onCancelled(error: DatabaseError) {}

        })




    }

    fun getAllSellersFromFireDB(){
        val sellers:MutableList<Customer> = ArrayList()
        val db = Firebase.database.reference.child("user")
        db.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(user in snapshot.children){
                    val customer = user.getValue(Customer::class.java)

                    if(customer!!.status=="seller")
                    {
                        Log.e("name",customer.status)
                        sellers.add(customer)

                    }

                }
                adapter.setData(sellers)
            }

            override fun onCancelled(error: DatabaseError) {}

        })




    }


    private fun setRecyclerView(){
        binding.userRecyclerID.layoutManager = LinearLayoutManager(requireContext())
        binding.userRecyclerID.adapter = adapter
        binding.userRecyclerID.itemAnimator = SlideInRightAnimator().apply {
            addDuration = 500
        }

    }


}