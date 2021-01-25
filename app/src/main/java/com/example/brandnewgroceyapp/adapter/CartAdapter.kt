package com.example.brandnewgroceyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.brandnewgroceyapp.databinding.CartItemLayoutBinding
import com.example.brandnewgroceyapp.model.Grocery
import com.example.brandnewgroceyapp.util.CartDiffUtil
import com.example.brandnewgroceyapp.util.CartListener
import com.example.brandnewgroceyapp.util.QuantityListener
import com.google.firebase.database.ValueEventListener

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartItemHolder>() {
    private var groceries: MutableList<Grocery> = ArrayList()
    private var cartListener: CartListener? = null
    private var quantityListener: QuantityListener? = null

    class CartItemHolder(itemView: CartItemLayoutBinding) : RecyclerView.ViewHolder(itemView.root) {

        val binding = itemView
        fun bind(grocery: Grocery) {
            binding.grocery = grocery
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemHolder {
        val view = CartItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemHolder(view)
    }

    override fun onBindViewHolder(holder: CartItemHolder, position: Int) {

        holder.bind(groceries[position])
        holder.binding.deleteIconID.setOnClickListener {
            cartListener!!.listen(groceries[position])
            groceries.remove(groceries[position])
            notifyDataSetChanged()
        }
        holder.binding.addButtonID.setOnClickListener {
            quantityListener!!.listenQuantity(true, groceries[position].price2)
            holder.binding.quantityTextID
            addQuantity(holder)
            //Toast.makeText(it.context,"add",Toast.LENGTH_SHORT).show()

        }
        holder.binding.removeButtonID.setOnClickListener {
            var quantity: Int = holder.binding.quantityTextID.text.toString().toInt()
             if(quantity>1){
                 quantityListener!!.listenQuantity(false, groceries[position].price2)
                 removeQuantity(holder)
             }

            //Toast.makeText(it.context,"add",Toast.LENGTH_SHORT).show()

        }

    }

    private fun removeQuantity(holder: CartItemHolder) {
        var quantity: Int = holder.binding.quantityTextID.text.toString().toInt()
        if (quantity > 1) {
            --quantity
            holder.binding.quantityTextID.text = quantity.toString()
        }
    }

    private fun addQuantity(holder: CartItemHolder) {
        var quantity: Int = holder.binding.quantityTextID.text.toString().toInt()
        ++quantity
        holder.binding.quantityTextID.text = quantity.toString()

    }


    override fun getItemCount(): Int {

        return groceries.size
    }

    fun setData(
        newGroceries: MutableList<Grocery>,
        cartListener: CartListener,
        quantityListener: QuantityListener
    ) {

        this.cartListener = cartListener
        this.quantityListener = quantityListener
        val diff = CartDiffUtil(groceries,newGroceries)
        groceries = newGroceries
        val cal = DiffUtil.calculateDiff(diff)
        cal.dispatchUpdatesTo(this)

    }
}