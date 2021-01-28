package com.example.brandnewgroceyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.brandnewgroceyapp.R
import com.example.brandnewgroceyapp.databinding.ChatViewLayoutBinding
import com.example.brandnewgroceyapp.model.ChatView
import com.example.brandnewgroceyapp.util.Listener
import com.example.brandnewgroceyapp.view.ChatViewBottomFragmentDirections

class ChatViewAdapter: RecyclerView.Adapter<ChatViewAdapter.ChatViewViewHolder>(),Listener {
    var chatViews:List<ChatView> = emptyList()
    class ChatViewViewHolder(itemView: ChatViewLayoutBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
        fun bind(chatView: ChatView){
            binding.chatView = chatView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewViewHolder {
        val view = ChatViewLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ChatViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewViewHolder, position: Int) {
        holder.bind(chatViews[position])
        holder.binding.listen = this
    }

    override fun getItemCount(): Int {
        return chatViews.size
    }
    fun setChatView(newChatViews:List<ChatView>){
        chatViews = newChatViews
        notifyDataSetChanged()
    }

    override fun clicked(view: View) {
        val id = view.findViewById<TextView>(R.id.chatViewUserID).text.toString()
        val status = view.findViewById<TextView>(R.id.chatViewStatus).text.toString()

        val action = ChatViewBottomFragmentDirections.actionChatViewBottomFragmentToStartChatingCloneFragment(id,status)
        Navigation.findNavController(view).navigate(action)
    }

}