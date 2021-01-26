package com.example.brandnewgroceyapp.util

import androidx.recyclerview.widget.DiffUtil
import com.example.brandnewgroceyapp.model.Customer

class CustomerDiffUtil(
    private val oldUsers:List<Customer>,
    private val newUsers:List<Customer>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldUsers.size
    }

    override fun getNewListSize(): Int {
        return newUsers.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldUsers[oldItemPosition]===newUsers[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldUsers[oldItemPosition]==newUsers[newItemPosition]
    }
}