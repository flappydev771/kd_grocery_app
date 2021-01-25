package com.example.brandnewgroceyapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.brandnewgroceyapp.adapter.CartAdapter
import com.example.brandnewgroceyapp.databinding.FragmentCartBinding
import com.example.brandnewgroceyapp.model.Grocery
import com.example.brandnewgroceyapp.util.CartListener
import com.example.brandnewgroceyapp.util.QuantityListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


@Suppress("NAME_SHADOWING")
class CartFragment : Fragment(), CartListener, QuantityListener {

    private lateinit var binding: FragmentCartBinding
    private lateinit var database: DatabaseReference
    private lateinit var choosenItems: MutableList<Grocery>
    private val id = Firebase.auth.currentUser!!.uid
    private val adapter: CartAdapter by lazy { CartAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        binding.checkoutButtonID.setOnClickListener {

            var total: String = binding.totalPriceID.text.toString()
            total = total.replace("৳", "")

            if (total.toDouble() < 1.0) {

                Toasty.warning(
                    requireContext(),
                    "Please add some items to cart",
                    Toasty.LENGTH_SHORT
                ).show()
            } else {
                val action = CartFragmentDirections.actionCartFragmentToPaymentFragment(total)
                findNavController().navigate(action)
            }
        }
        choosenItems = ArrayList()
        database = Firebase.database.reference

        setRecyclerView()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getAllDataFromDB()
    }

    private fun setRecyclerView() {
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = adapter
        binding.cartRecyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 1500
        }
    }

    private fun getAllDataFromDB() {
        val id = Firebase.auth.currentUser!!.uid
        database.child("cart").child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        choosenItems.clear()
                        for (item in snapshot.children) {
                            val name = item.child("name").value.toString()
                            val category = item.child("category").value.toString()
                            val description = item.child("description").value.toString()
                            val id = item.child("id").value.toString().toInt()
                            val image = item.child("image").value.toString()
                            val price1 = item.child("price1").value.toString()
                            val price2 = item.child("price2").value.toString()
                            val stock = item.child("stock").value.toString()
                            val grocery = Grocery(
                                id,
                                name,
                                category,
                                price1,
                                price2,
                                stock,
                                description,
                                image
                            )
                            choosenItems.add(grocery)

                            Toast.makeText(requireContext(), stock, Toast.LENGTH_SHORT).show()
                        }
                        if (choosenItems.size > 0) {
                            var price: Int = 0
                            for (singleItem in choosenItems) {
                                price += singleItem.price2.toInt()
                            }
                            var totalPrice: Double = price + 50 + (price * 0.1)
                            binding.subTotalID.text = price.toString() + "৳"
                            binding.totalPriceID.text = totalPrice.toString() + "৳"
                        } else {
                            binding.subTotalID.text = "0৳"
                            binding.totalPriceID.text = "0৳"
                        }
                        adapter.setData(choosenItems, this@CartFragment, this@CartFragment)
                    } else {
                        binding.subTotalID.text = "0৳"
                        binding.totalPriceID.text = "0৳"
                    }
                }

            })

    }

    override fun listen(grocery: Grocery) {

        deleteItemFronDB(grocery)
        deleteFromAdded(grocery)
        decreseCount()
        getAllDataFromDB()

    }

    private fun decreseCount() {
        database.child("cart").child("count").child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var value: Int = snapshot.child("num").value.toString().toInt()
                    value = value - 1
                    database.child("cart").child("count").child(id).child("num")
                        .setValue(value)

                }

            })
    }

    private fun deleteFromAdded(grocery: Grocery) {
        database.child("cart").child("added").child(id).child(grocery.id.toString()).removeValue()
    }

    private fun deleteItemFronDB(grocery: Grocery) {

        database.child("cart").child(id).child(grocery.id.toString())
            .removeValue()

    }

    @SuppressLint("SetTextI18n")
    override fun listenQuantity(state: Boolean, price: String) {
        val targetPrice: Int = price.toInt()
        var subtotalPriceStr: String = binding.subTotalID.text.toString()
        subtotalPriceStr = subtotalPriceStr.replace("৳", "")
        var subtotalPrice = subtotalPriceStr.toInt()

        val totalPrice: Double
        if (state) {
            subtotalPrice += targetPrice
            binding.subTotalID.text = subtotalPrice.toString() + "৳"
            totalPrice = subtotalPrice + 50 + (subtotalPrice * 0.1)
            binding.totalPriceID.text = totalPrice.toString() + "৳"
        } else {
            if (subtotalPrice > targetPrice) {
                subtotalPrice -= targetPrice
                binding.subTotalID.text = subtotalPrice.toString() + "৳"
                totalPrice = subtotalPrice + 50 + (subtotalPrice * 0.1)
                binding.totalPriceID.text = totalPrice.toString() + "৳"
            } else {
                subtotalPrice = targetPrice
                binding.subTotalID.text = subtotalPrice.toString() + "৳"
                totalPrice = subtotalPrice + 50 + (subtotalPrice * 0.1)
                binding.totalPriceID.text = totalPrice.toString() + "৳"
            }


        }
        /*if (state) {
            Toast.makeText(requireContext(), "add " + subtotalPriceStr, Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(requireContext(), "remove " + subtotalPriceStr, Toast.LENGTH_SHORT)
                .show()

        }*/

    }


}