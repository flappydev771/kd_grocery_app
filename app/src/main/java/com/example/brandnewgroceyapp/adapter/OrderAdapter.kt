package com.example.brandnewgroceyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.brandnewgroceyapp.databinding.OrderLayoutBinding
import com.example.brandnewgroceyapp.model.ItemOrder
import com.example.brandnewgroceyapp.util.OrderDiffUtil

class OrderAdapter: RecyclerView.Adapter<OrderAdapter.OrderHolder>() {

     var orders:MutableList<ItemOrder> = ArrayList()

    class OrderHolder(itemView: OrderLayoutBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
        fun bindOrder(order: ItemOrder){
            binding.order = order
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val view = OrderLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {

        holder.bindOrder(orders[position])
    }

    override fun getItemCount(): Int {
        return orders.size
    }
    fun setData(newOrders:MutableList<ItemOrder>){
       val ordeDiffUtil = OrderDiffUtil(orders,newOrders)
        orders = newOrders
        val calculateDiff = DiffUtil.calculateDiff(ordeDiffUtil)
        calculateDiff.dispatchUpdatesTo(this)

    }
    fun deleteItem(position: Int){
        val item = orders.get(position)
        orders.remove(item)
        notifyItemRemoved(position)
    }
}