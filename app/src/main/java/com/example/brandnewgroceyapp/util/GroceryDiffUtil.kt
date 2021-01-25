package com.example.brandnewgroceyapp.util

import androidx.recyclerview.widget.DiffUtil
import com.example.brandnewgroceyapp.model.Grocery

class GroceryDiffUtil(
    private val oldResult:List<Grocery>,
    private val newResult: List<Grocery>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldResult.size
    }

    override fun getNewListSize(): Int {
        return newResult.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldResult[oldItemPosition]===newResult[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldResult[oldItemPosition]==newResult[newItemPosition]

    }
}