package com.example.brandnewgroceyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.brandnewgroceyapp.R
import com.example.brandnewgroceyapp.databinding.UserListLayoutBinding
import com.example.brandnewgroceyapp.model.Customer
import com.example.brandnewgroceyapp.util.CustomerDiffUtil
import com.example.brandnewgroceyapp.util.Listener
import com.example.brandnewgroceyapp.view.ChatFragmentDirections
import com.example.brandnewgroceyapp.view.StartChatingFragmentDirections

class ChatUserAdapter: RecyclerView.Adapter<ChatUserAdapter.UserHolder>(),Listener {
    private var users:List<Customer> = emptyList()
    class UserHolder(itemView: UserListLayoutBinding) : RecyclerView.ViewHolder(itemView.root) {
     val binding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {

        val view = UserListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserHolder(view)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
    holder.binding.customer = users[position]
        holder.binding.listener = this

    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun clicked(view: View) {
        val id = view.findViewById<TextView>(R.id.customerID)
        val status = view.findViewById<TextView>(R.id.customerStatus)

        val action = ChatFragmentDirections.actionChatFragmentToStartChatingFragment(id.text.toString(),status.text.toString())
        Navigation.findNavController(view).navigate(action)


    }
    fun setData(newUsers:List<Customer>){
        val diff = CustomerDiffUtil(users,newUsers)
        users = newUsers
        val cal = DiffUtil.calculateDiff(diff)
        cal.dispatchUpdatesTo(this)

    }
}