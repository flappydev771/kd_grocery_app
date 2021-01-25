package com.example.brandnewgroceyapp.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import berlin.volders.badger.CountBadge
import com.example.brandnewgroceyapp.R
import com.example.brandnewgroceyapp.adapter.GroceryAdapter
import com.example.brandnewgroceyapp.model.Grocery
import com.example.brandnewgroceyapp.util.CartListener
import com.example.brandnewgroceyapp.util.NetworkState
import com.example.brandnewgroceyapp.util.Util
import com.example.brandnewgroceyapp.viewmodel.GroceryByCategoryViewModel
import com.glide.slider.library.animations.SliderAnimationInterface
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.todkars.shimmer.ShimmerRecyclerView
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroceryByCategoryFragment : Fragment(), SearchView.OnQueryTextListener,CartListener {


    private lateinit var dot: DilatingDotsProgressBar
    private lateinit var myView: View
    private lateinit var viewMode: GroceryByCategoryViewModel
    private lateinit var categoryText: TextView
    private var count: Int = 1
    private lateinit var recycler:ShimmerRecyclerView
    private lateinit var database: DatabaseReference
    val adapter: GroceryAdapter by lazy { GroceryAdapter() }

    val args: GroceryByCategoryFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewMode = ViewModelProvider(this).get(GroceryByCategoryViewModel::class.java)
        Log.e("Cat:", args.category)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        database = Firebase.database.reference.child("cart")
        val view = inflater.inflate(R.layout.fragment_grocery_by_category, container, false)
        myView = view
        recycler = view.findViewById(R.id.groceryByCategoryRecyclerID)
        dot = view.findViewById(R.id.dotProgressID)
        categoryText = view.findViewById(R.id.categoryTextID)
        fetchGroceryByCategory()
        getGroceryByCategory(view)
        setGroceryRecycler()
        return view
    }

    fun fetchGroceryByCategory() {
        viewMode.getGroceryByCategory(args.category)
    }

    fun getGroceryByCategory(view: View) {
        viewMode.groceryByCategory.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetworkState.Load -> {

                    showShimmer()
                    Util.showDotProgress(dot)
                    categoryText.visibility = View.GONE

                }

                is NetworkState.Error -> {
                    hideShimmer()
                    categoryText.visibility = View.GONE
                    Util.hideDotProgress(dot)
                    Log.e("Error", "error")
                }
                is NetworkState.Success -> {
                    hideShimmer()
                    categoryText.visibility = View.VISIBLE
                    categoryText.text = args.category
                    Util.hideDotProgress(dot)
                    adapter.setGrocery(response.data!!.groceries,this)
                }

            }

        })
    }

    fun setGroceryRecycler() {

        val layoutManager: LinearLayoutManager = LinearLayoutManager(requireContext())
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter

        recycler.itemAnimator = LandingAnimator().apply {
            addDuration = 1000

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
            getGroceryByCategory(myView)
        }

        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {

        if (query != null && !query.isEmpty()) {

            getDataByname(query)
        } else {
            getGroceryByCategory(myView)
        }

        return true
    }

    fun getDataByname(name: String) {
        val searcQuery = "%$name%"
        val category = "%${args.category}%"
        viewMode.groceryByName(searcQuery,category).observe(viewLifecycleOwner, Observer { groceries ->

            groceries.let {

                adapter.setGrocery(it,this)
            }
        })
    }

    fun showShimmer(){
        recycler.showShimmer()
    }
    fun hideShimmer(){
        recycler.hideShimmer()
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