package com.example.brandnewgroceyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.brandnewgroceyapp.databinding.OtherUserChatLayoutBinding
import com.example.brandnewgroceyapp.databinding.UserChatLayoutBinding
import com.example.brandnewgroceyapp.model.ChatMessage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatingAdapater : RecyclerView.Adapter<ChatingAdapater.ChatHolder>() {
    private var messages: List<ChatMessage> = emptyList()



    class ChatHolder : RecyclerView.ViewHolder {
        var oneUserBinding: UserChatLayoutBinding? = null
        private var otherUserBinding: OtherUserChatLayoutBinding? = null

        constructor(binding: UserChatLayoutBinding) : super(binding.root) {
            oneUserBinding = binding
        }

        constructor(binding: OtherUserChatLayoutBinding) : super(binding.root) {
            otherUserBinding = binding
        }

        fun bindOneUser(message: ChatMessage) {
            oneUserBinding!!.chat = message
        }

        fun bindOtherUser(message: ChatMessage) {
            otherUserBinding!!.chat = message
        }

    }


    override fun getItemViewType(position: Int): Int {
        if (messages[position].from == Firebase.auth.currentUser!!.uid) {
            return 1
        } else {
            return 2
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val view1 = UserChatLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val view2 = OtherUserChatLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        if (viewType == 1) {
            return ChatHolder(view1)
        } else {
            return ChatHolder(view2)
        }


    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        if (holder.itemViewType == 1) {
            holder.bindOneUser(messages[position])
        } else {
            holder.bindOtherUser(messages[position])
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun setData(newMessages: List<ChatMessage>) {
        messages = newMessages
        notifyDataSetChanged()

    }
}