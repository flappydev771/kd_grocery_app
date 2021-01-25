package com.example.brandnewgroceyapp.util

import androidx.recyclerview.widget.DiffUtil
import com.example.brandnewgroceyapp.model.ItemOrder

class OrderDiffUtil(
    private val oldOrder: List<ItemOrder>,
    private val newOrder: List<ItemOrder>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldOrder.size
    }

    override fun getNewListSize(): Int {
        return newOrder.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldOrder[oldItemPosition] === newOrder[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldOrder[oldItemPosition] == newOrder[newItemPosition]
    }
}