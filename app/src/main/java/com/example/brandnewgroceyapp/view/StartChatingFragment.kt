package com.example.brandnewgroceyapp.view

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.brandnewgroceyapp.R
import com.example.brandnewgroceyapp.adapter.ChatingAdapater
import com.example.brandnewgroceyapp.databinding.FragmentStartChatingBinding
import com.example.brandnewgroceyapp.model.ChatMessage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.LandingAnimator


class StartChatingFragment : Fragment() {
    val currentUserId = Firebase.auth.currentUser!!.uid
    var chatMessages: MutableList<ChatMessage> = ArrayList()
    private lateinit var binding: FragmentStartChatingBinding
    val adapter: ChatingAdapater by lazy { ChatingAdapater() }
    private lateinit var database: DatabaseReference
    private lateinit var mediaPlayer: MediaPlayer
    private var notFirstTime: Boolean = false
    private val args: StartChatingFragmentArgs by navArgs()

    override fun onStart() {
        super.onStart()
        notFirstTime = false
        getChatAndAddToAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.fb)
        binding = FragmentStartChatingBinding.inflate(inflater, container, false)
        database = Firebase.database.reference.child("chat")
        binding.chatRecyclerID.layoutManager = LinearLayoutManager(requireContext())
        binding.chatRecyclerID.adapter = adapter
        binding.chatRecyclerID.itemAnimator = LandingAnimator().apply {
            addDuration = 2000
        }

        binding.chatFab.setOnClickListener {

            if (binding.msgEditText.text.toString().trim().isEmpty()) {
                Toasty.warning(requireContext(), "Please write something !", Toasty.LENGTH_SHORT)
                    .show()
            } else {
                val message = binding.msgEditText.text.toString().trim()
                val msg = ChatMessage(currentUserId, System.currentTimeMillis().toString(), message)
                val status = args.status

                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
                Log.d("STATE", status)
                if (status == "customer") {
                    addToDB(msg, currentUserId, args.id)
                } else {
                    addToDB(msg, args.id, currentUserId)
                }


            }


        }

        return binding.root
    }

    private fun getChatAndAddToAdapter() {
        val status = args.status
        if (status == "customer") {
            getFromDB(currentUserId, args.id)
        } else {
            getFromDB(args.id, currentUserId)
        }

    }


    private fun setChatRecycler(chatMessages: List<ChatMessage>) {

        binding.chatRecyclerID.smoothScrollToPosition(chatMessages.size - 1)
        adapter.setData(chatMessages)
    }

    fun addToDB(message: ChatMessage, currentID: String, otherID: String) {
        database.child(currentID).child(otherID)
            .child(database.push().key.toString()).setValue(message)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getChatAndAddToAdapter()
                    binding.msgEditText.text.clear()
                    Toasty.success(requireContext(), "sent", Toasty.LENGTH_SHORT).show()

                } else {
                    Toasty.warning(
                        requireContext(),
                        "Connection Error... !",
                        Toasty.LENGTH_SHORT
                    ).show()

                }

            }
    }


    fun getFromDB(currentID: String, otherID: String) {
        database.child(currentID).child(otherID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatMessages.clear()
                    if (snapshot.exists()) {
                        for (chat in snapshot.children) {
                            val chatMessage = chat.getValue(ChatMessage::class.java)
                            chatMessages.add(chatMessage!!)
                        }
                        if (chatMessages[chatMessages.size - 1].from != Firebase.auth.currentUser!!.uid) {
                            if (notFirstTime == true) {
                                mediaPlayer.start()
                            } else {
                                notFirstTime = true
                            }


                        }
                        setChatRecycler(chatMessages)
                    }

                }

                override fun onCancelled(error: DatabaseError) {}

            })
    }

}