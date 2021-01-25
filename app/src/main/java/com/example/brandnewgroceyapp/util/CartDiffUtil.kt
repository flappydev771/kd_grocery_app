package com.example.brandnewgroceyapp.util

import androidx.recyclerview.widget.DiffUtil
import com.example.brandnewgroceyapp.model.Grocery

class CartDiffUtil(
    private val oldGrocery: List<Grocery>,
    private val newGrocery: List<Grocery>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldGrocery.size
    }

    override fun getNewListSize(): Int {
        return newGrocery.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldGrocery[oldItemPosition] === newGrocery[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldGrocery[oldItemPosition] == newGrocery[newItemPosition]

    }
}