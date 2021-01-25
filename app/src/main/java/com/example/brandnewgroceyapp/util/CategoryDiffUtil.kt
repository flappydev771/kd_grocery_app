package com.example.brandnewgroceyapp.util

import androidx.recyclerview.widget.DiffUtil
import com.example.brandnewgroceyapp.model.Category

class CategoryDiffUtil(
    private val oldCategories: List<Category>,
    private val newCategories: List<Category>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldCategories.size
    }

    override fun getNewListSize(): Int {
        return newCategories.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCategories[oldItemPosition] === newCategories[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCategories[oldItemPosition] == newCategories[newItemPosition]
    }
}