package com.example.brandnewgroceyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.brandnewgroceyapp.databinding.ChatViewLayoutBinding
import com.example.brandnewgroceyapp.model.ChatView

class ChatViewAdapter: RecyclerView.Adapter<ChatViewAdapter.ChatViewViewHolder>() {
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
    }

    override fun getItemCount(): Int {
        return chatViews.size
    }
    fun setChatView(newChatViews:List<ChatView>){
        chatViews = newChatViews
        notifyDataSetChanged()
    }

}