package com.example.brandnewgroceyapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.brandnewgroceyapp.R
import com.example.brandnewgroceyapp.adapter.ChatViewAdapter
import com.example.brandnewgroceyapp.model.ChatView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class ChatViewBottomFragment : Fragment() {

    private lateinit var chatViewRecycle: RecyclerView
    private val adapter by lazy { ChatViewAdapter() }
    private var chatViews: MutableList<ChatView> = ArrayList()
    private lateinit var database: DatabaseReference

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            getAllChatViews()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat_view_bottom, container, false)
        database = Firebase.database.reference.child("chatView")
        chatViewRecycle = view.findViewById(R.id.chatViewRecyclerID)
        chatViewRecycle.setHasFixedSize(true)
        chatViewRecycle.layoutManager = LinearLayoutManager(requireContext())
        chatViewRecycle.adapter = adapter
        return view
    }

    private suspend fun getAllChatViews() {
        val id = Firebase.auth.currentUser!!.uid
        database.child("view").child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    chatViews.clear()
                    for (data in snapshot.children) {
                        val chatView = data.getValue(ChatView::class.java)
                        chatViews.add(chatView!!)
                        Log.e("View",chatView.status)

                    }
                    adapter.setChatView(chatViews.reversed())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}