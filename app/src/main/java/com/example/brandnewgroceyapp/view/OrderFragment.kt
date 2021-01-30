package com.example.brandnewgroceyapp.view

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.brandnewgroceyapp.R
import com.example.brandnewgroceyapp.adapter.OrderAdapter
import com.example.brandnewgroceyapp.model.ItemOrder
import com.example.brandnewgroceyapp.util.SwipeToDelete
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import javax.inject.Inject

@AndroidEntryPoint
class OrderFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private val id = Firebase.auth.currentUser!!.uid
    private lateinit var text:TextView
    private lateinit var layout:LinearLayout
    private lateinit var orderRecyclerView: RecyclerView
    private val adapter by lazy { OrderAdapter() }
    private var orders:MutableList<ItemOrder> = ArrayList()
    @Inject lateinit var sharedPreferences: SharedPreferences

    override fun onStart() {
        super.onStart()
        checkOrder(id)
        checkOrderForSeller()
    }

    private fun checkOrderForSeller() {

        val status = sharedPreferences.getString("state","")
        if(status == "seller"){

           database.child("order").addValueEventListener(object :ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   orders.clear()
                   for(child in snapshot.children){
                       for(data in child.children){
                           val order = data.getValue(ItemOrder::class.java)
                           orders.add(order!!)
                       }
                   }
                   if(orders.size==0){
                       layout.visibility = View.VISIBLE
                       adapter.setData(orders)
                       adapter.notifyDataSetChanged()
                   }
                   else{
                       layout.visibility = View.GONE
                       adapter.setData(orders.asReversed())
                       adapter.notifyDataSetChanged()
                   }

               }

               override fun onCancelled(error: DatabaseError) {
                   TODO("Not yet implemented")
               }

           })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = Firebase.database.reference
        val view =inflater.inflate(R.layout.fragment_order, container, false)
        //text = view.findViewById(R.id.test)

        layout = view.findViewById(R.id.showNoOrder)
        initRecycler(view)
        swipeToDelete(orderRecyclerView)

        return view
    }

    private fun initRecycler(view: View) {
        orderRecyclerView = view.findViewById(R.id.orderRecyclerID)
        orderRecyclerView.setHasFixedSize(true)
        orderRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        orderRecyclerView.adapter = adapter

        orderRecyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }


    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.orders[viewHolder.adapterPosition]
                adapter.deleteItem(viewHolder.adapterPosition)
                deleteFromDB(id,deletedItem)
                checkOrder(id)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun deleteFromDB(id: String, order: ItemOrder) {
        database.child("order").child(id).child(order.id).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                Toasty.success(requireContext(),"Order removed successfully",Toasty.LENGTH_SHORT).show()
            }
        }
    }


    private fun checkOrder(id: String) {

        database.child("order").child(id).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    layout.visibility = View.GONE
                    getAllOrders(id)
                }
                else{
                   layout.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun getAllOrders(id: String) {
        database.child("order").child(id).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    val order = data.getValue(ItemOrder::class.java)
                   orders.add(order!!)
                }
                adapter.setData(orders)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }


}