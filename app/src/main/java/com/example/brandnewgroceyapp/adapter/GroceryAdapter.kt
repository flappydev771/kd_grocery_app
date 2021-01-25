package com.example.brandnewgroceyapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.brandnewgroceyapp.R
import com.example.brandnewgroceyapp.databinding.AllGroceryLayoutBinding
import com.example.brandnewgroceyapp.model.Grocery
import com.example.brandnewgroceyapp.util.CartListener
import com.example.brandnewgroceyapp.util.GroceryDiffUtil
import com.example.brandnewgroceyapp.view.HomeFragment
import es.dmoral.toasty.Toasty


class GroceryAdapter: RecyclerView.Adapter<GroceryAdapter.GroceryHolder>() {

    private var groceries = emptyList<Grocery>()
    private var cartListener: CartListener? = null


    class GroceryHolder(itemView: AllGroceryLayoutBinding) : RecyclerView.ViewHolder(itemView.root)
        {

        val binding = itemView
      companion object{
          fun from(context: Context, viewGroup: ViewGroup):AllGroceryLayoutBinding{
              val inflater = LayoutInflater.from(context)
              return AllGroceryLayoutBinding.inflate(inflater, viewGroup, false)
          }
      }
        fun bind(grocery: Grocery){
            binding.grocery = grocery
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryHolder {
        return GroceryHolder(GroceryHolder.from(parent.context, parent))
    }

    override fun onBindViewHolder(holder: GroceryHolder, position: Int) {

        holder.bind(groceries[position])
        holder.binding.addToCartID.setOnClickListener {
            cartListener!!.listen(groceries[position])
        }

    }

    override fun getItemCount(): Int {
        return groceries.size
    }

    fun setGrocery(newGroceries: List<Grocery>,cartListener: CartListener){
        this.cartListener = cartListener

       val groceryDiff = GroceryDiffUtil(groceries, newGroceries)
        groceries = newGroceries
      val diff = DiffUtil.calculateDiff(groceryDiff)
        diff.dispatchUpdatesTo(this)

    }


}