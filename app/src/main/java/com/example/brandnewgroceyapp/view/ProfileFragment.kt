package com.example.brandnewgroceyapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.brandnewgroceyapp.R
import com.example.brandnewgroceyapp.databinding.FragmentProfileBinding
import com.example.brandnewgroceyapp.model.Customer
import com.example.brandnewgroceyapp.util.Util
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import es.dmoral.toasty.Toasty


class ProfileFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentProfileBinding
    private val currentUserID = Firebase.auth.currentUser!!.uid
    private lateinit var database: DatabaseReference
    private lateinit var userDB: DatabaseReference


    override fun onStart() {
        super.onStart()
        checkProfile()

    }

    private fun checkProfile() {
        val id = Firebase.auth.currentUser!!.uid
        database.child("profile")
            .child(id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        binding.noProfileError.visibility = View.GONE
                        binding.profileFound.visibility = View.VISIBLE
                        addExistingValue()
                    } else {
                        binding.noProfileError.visibility = View.VISIBLE
                        binding.profileFound.visibility = View.GONE

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        userDB = Firebase.database.reference.child("user")
        database = Firebase.database.reference
        return binding.root
    }

    private fun addExistingValue() {
        userDB.child(currentUserID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val email = snapshot.child("email").value.toString()
                    val name = snapshot.child("name").value.toString()
                    var image = snapshot.child("image").value.toString()
                    val pass = snapshot.child("password").value.toString()
                    val status = snapshot.child("status").value.toString()
                    val address = snapshot.child("address").value.toString()
                    val id = snapshot.child("id").value.toString()
                    var phone: String = ""
                    if (snapshot.child("phone").exists()) {
                        phone = snapshot.child("phone").value.toString()
                    } else {
                        phone = ""
                    }
                    if (!image.isEmpty()) {
                        Glide.with(requireContext()).load(image)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .into(binding.profileImageID)
                    }
                    val user = Customer(id, name, email, pass, status, image, address, phone)

                    binding.profileNameID.setText(name)
                    binding.profileEmailID.setText(email)
                    binding.profileAddressID.setText(address)
                    binding.profilePhnID.setText(phone)

                    /// Toast.makeText(requireContext(), sharedPreferences.getString(Util.USER_PIC,""), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}