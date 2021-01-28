package com.example.brandnewgroceyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.example.brandnewgroceyapp.R
import com.example.brandnewgroceyapp.databinding.CategoryLayoutBinding
import com.example.brandnewgroceyapp.model.Category
import com.example.brandnewgroceyapp.util.BindGroceries
import com.example.brandnewgroceyapp.util.CategoryDiffUtil
import com.example.brandnewgroceyapp.util.GroceryLisener
import com.example.brandnewgroceyapp.view.ChatFragmentDirections
import com.example.brandnewgroceyapp.view.HomeFragmentDirections
import de.hdodenhof.circleimageview.CircleImageView
import es.dmoral.toasty.Toasty

class CategoryAdapter : GroceryLisener, RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {
    private var categories: List<Category> = emptyList()

    class CategoryHolder(itemView: CategoryLayoutBinding) : RecyclerView.ViewHolder(itemView.root) {

        val binding = itemView

        fun bind(category: Category) {
            binding.category = category
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view = CategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {


        holder.binding.listen = this

        holder.bind(categories[position])


    }

    override fun getItemCount(): Int {
        return categories.size

    }

    override fun listen(view: View) {
        val name = view.findViewById<TextView>(R.id.categoryNameID).text.toString()
        Toasty.success(view.context, name, Toasty.LENGTH_SHORT).show()
        val action = HomeFragmentDirections.actionHomeFragmentToGroceryByCategoryFragment(name)
        Navigation.findNavController(view).navigate(action)

    }

    fun setCategory(newCategories:List<Category>){
        val diff = CategoryDiffUtil(categories,newCategories)
        categories = newCategories
        val cal = DiffUtil.calculateDiff(diff)
        cal.dispatchUpdatesTo(this)

    }

}