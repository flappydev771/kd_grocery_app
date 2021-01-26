package com.example.brandnewgroceyapp.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.brandnewgroceyapp.R
import com.example.brandnewgroceyapp.adapter.GroceryAdapter
import com.example.brandnewgroceyapp.model.Grocery
import com.example.brandnewgroceyapp.util.CartListener
import com.example.brandnewgroceyapp.util.NetworkState
import com.example.brandnewgroceyapp.util.Util
import com.example.brandnewgroceyapp.viewmodel.GroceryByCategoryViewModel
import com.example.brandnewgroceyapp.viewmodel.MainViewModel
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
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ViewAllFragment : Fragment(), CartListener, SearchView.OnQueryTextListener {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var dot: DilatingDotsProgressBar
    private var count: Int = 1
    private lateinit var layout: LinearLayout
    private lateinit var groceryRecyclerID: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var viewMode: GroceryByCategoryViewModel

    private lateinit var myView: View
    val groceryAdapter: GroceryAdapter by lazy { GroceryAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewMode = ViewModelProvider(this).get(GroceryByCategoryViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_view_all, container, false)
        layout = view.findViewById(R.id.showNoItem)
        database = Firebase.database.reference.child("cart")
        myView = view
        dot = view.findViewById(R.id.dotProgress)
        fetchAllGroceries()
        setAllGroceries(view)
        setGroceryAdapter(view)
        return view
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

                    Util.hideDotProgress(dot)
                    groceryAdapter.setGrocery(response.data!!.groceries, this, 1)
                    Log.e("Image", response.data.groceries.get(2).name)

                }

            }


        })
    }

    private fun setGroceryAdapter(view: View) {

        groceryRecyclerID = view.findViewById(R.id.allGroceryRecyclerID)
        val layoutManager: LinearLayoutManager =
            LinearLayoutManager(requireContext())
        groceryRecyclerID.layoutManager = layoutManager
        groceryRecyclerID.adapter = groceryAdapter
        groceryRecyclerID.itemAnimator = LandingAnimator().apply {
            addDuration = 500
        }
    }

    override fun listen(grocery: Grocery) {
        lifecycleScope.launch(Dispatchers.IO) {
            addToCart(grocery)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item_search, menu)
        val search = menu.findItem(R.id.action_search_id)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null && !query.isEmpty()) {

            getDataByname(query)
        } else {
            setAllGroceries(myView)
        }

        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null && !query.isEmpty()) {

            getDataByname(query)
        } else {
            setAllGroceries(myView)
        }

        return true
    }

    fun getDataByname(name: String) {
        val searcQuery = "%$name%"
        viewMode.groceryByNameOnly(searcQuery).observe(viewLifecycleOwner, Observer { groceries ->

            groceries.let {

                if (groceries.size==0) {
                    layout.visibility = View.VISIBLE

                } else {
                    layout.visibility = View.GONE
                }
                groceryAdapter.setGrocery(it, this, 1)

            }
        })
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


                    } else {
                        val map = mapOf(
                            "num" to count
                        )
                        database.child("count").child(id).updateChildren(map)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }
}