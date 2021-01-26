@file:Suppress("UNREACHABLE_CODE")

package com.example.brandnewgroceyapp.view

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.compose.runtime.snapshots.takeSnapshot
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import berlin.volders.badger.BadgeDrawable
import berlin.volders.badger.BadgeShape
import berlin.volders.badger.Badger
import berlin.volders.badger.CountBadge
import com.example.brandnewgroceyapp.R
import com.example.brandnewgroceyapp.adapter.CategoryAdapter
import com.example.brandnewgroceyapp.adapter.GroceryAdapter
import com.example.brandnewgroceyapp.model.Grocery
import com.example.brandnewgroceyapp.util.CartListener
import com.example.brandnewgroceyapp.util.NetworkState
import com.example.brandnewgroceyapp.util.Util
import com.example.brandnewgroceyapp.viewmodel.MainViewModel
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.LandingAnimator
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment(), CartListener {

    private lateinit var categoryRecyclerID: RecyclerView
    private lateinit var groceryRecyclerID: RecyclerView
    private lateinit var mainViewModel: MainViewModel
    private lateinit var database: DatabaseReference
    private var ovalFactory: CountBadge.Factory? = null
    private var count: Int = 1
    private lateinit var allViewButton: MaterialButton
    private lateinit var dot: DilatingDotsProgressBar
    private lateinit var badge: CountBadge

    val groceryAdapter: GroceryAdapter by lazy { GroceryAdapter() }

    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter()
    }
    lateinit var layoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        val id = Firebase.auth.currentUser!!.uid
        increaseBadge(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        ovalFactory = CountBadge.Factory(
            BadgeShape.square(0.7f, Gravity.TOP),
            ContextCompat.getColor(requireContext(), R.color.orange),
            ContextCompat.getColor(requireContext(), R.color.white)

        )

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        allViewButton = view.findViewById(R.id.viewAllButtonID)
        dot = view.findViewById(R.id.dotProgress)
        database = Firebase.database.reference.child("cart")

        allViewButton.setOnClickListener {
            ///Toast.makeText(requireContext(),"work",Toast.LENGTH_SHORT).show()
           findNavController().navigate(R.id.action_homeFragment_to_viewAllFragment)
        }

        Util.setSliders(view)

        fetchAllGroceries()
        setAllGroceries(view)

        setCategoryAdapter(view)

        setGroceryAdapter(view)


        return view
    }

    private fun setCategoryAdapter(view: View) {

        categoryRecyclerID = view.findViewById(R.id.categoryRecyclerID)
        categoryRecyclerID.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        categoryRecyclerID.layoutManager = layoutManager
        categoryRecyclerID.adapter = categoryAdapter

        categoryAdapter.setCategory(
            Util.getAllCategories(Util.getCategoryText(), Util.getCategoryImages())
        )

    }


    private fun fetchAllGroceries() {
        mainViewModel.getAllGroceries()
    }

    private fun setAllGroceries(view: View) {
        mainViewModel.allGroceries.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is NetworkState.Load -> {
                    Log.e("Load", "loading")

                    Util.showDotProgress(dot)

                }
                is NetworkState.Error -> {

                    Util.hideDotProgress(dot)
                    Log.e("Error", "Some Error")
                }
                is NetworkState.Success -> {

                    increaseBadge(Firebase.auth.currentUser!!.uid)

                    response.data!!.groceries.let {
                        for (item in it) {
                            Log.e("ITEM", item.name + " :" + item.id)
                            mainViewModel.cacheDataLocally(item)
                        }
                    }
                    Util.hideDotProgress(dot)
                    groceryAdapter.setGrocery(response.data.groceries, this, 6)
                    allViewButton.visibility = View.VISIBLE
                    Log.e("Image", response.data.groceries.get(2).name)

                }

            }


        })
    }

    private fun setGroceryAdapter(view: View) {

        groceryRecyclerID = view.findViewById(R.id.groceryRecyclerID)
        val layoutManager: LinearLayoutManager =
            LinearLayoutManager(requireContext())
        groceryRecyclerID.layoutManager = layoutManager
        groceryRecyclerID.adapter = groceryAdapter
        groceryRecyclerID.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 500
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val item: MenuItem = menu.findItem(R.id.action_badge)
        badge = Badger.sett(item, ovalFactory!!)
        badge.count = 0
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_badge) {
            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun listen(grocery: Grocery) {
        lifecycleScope.launch(Dispatchers.IO) {
            addToCart(grocery)
        }

    }

    private fun addToCart(grocery: Grocery) {
        val id = Firebase.auth.currentUser!!.uid

        database.child("added").child(id)
            .child(grocery.id.toString()).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Toasty.warning(
                            requireContext(),
                            grocery.name + "is already added to the cart.",
                            Toasty.LENGTH_SHORT
                        ).show()

                    } else {
                        val map: HashMap<String, Any> = HashMap()
                        map.put(grocery.id.toString(), "1")
                        database.child("added").child(id).child(grocery.id.toString())
                            .updateChildren(map).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    add(grocery, id)
                                } else {
                                    Toasty.error(
                                        requireContext(),
                                        grocery.name + "This item could not be added due to connection error",
                                        Toasty.LENGTH_SHORT
                                    ).show()
                                }

                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


    }

    private fun addCount(id: String) {
        database.child("count").child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val num: String = snapshot.child("num").value.toString()
                        val n: Int = num.toInt() + 1
                        val map = mapOf(
                            "num" to n
                        )
                        database.child("count").child(id).updateChildren(map)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    increaseBadge(id)
                                }

                            }


                    } else {
                        val map = mapOf(
                            "num" to count
                        )
                        database.child("count").child(id).updateChildren(map)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    increaseBadge(id)
                                }

                            }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }

    private fun increaseBadge(id: String) {
        database.child("count").child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val num: String = snapshot.child("num").value.toString()
                        val n: Int = num.toInt()
                        badge.count = n
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun add(grocery: Grocery, id: String) {
        database.child(id).child(grocery.id.toString()).setValue(grocery)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    addCount(id)
                    Toasty.success(
                        requireContext(),
                        grocery.name + " is added to the cart...",
                        Toasty.LENGTH_SHORT
                    ).show()

                } else {
                    Toasty.error(
                        requireContext(),
                        grocery.name + "This item could not be added due to connection error",
                        Toasty.LENGTH_SHORT
                    ).show()

                }

            }
    }


}