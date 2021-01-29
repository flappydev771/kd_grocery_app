package com.example.brandnewgroceyapp.view

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.disklrucache.DiskLruCache
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.brandnewgroceyapp.databinding.FragmentAccountBinding
import com.example.brandnewgroceyapp.model.ChatMessage
import com.example.brandnewgroceyapp.model.Customer
import com.example.brandnewgroceyapp.util.Util
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import javax.inject.Inject


@Suppress("DEPRECATION")
@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var userDB: DatabaseReference
    private val currentUserID = Firebase.auth.currentUser!!.uid
    private lateinit var binding: FragmentAccountBinding
    private lateinit var defDatabase:DatabaseReference
    private lateinit var progressDialog: ProgressDialog
    val storageRef = Firebase.storage.reference
    @Inject
    lateinit var editor: SharedPreferences.Editor
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onStart() {
        super.onStart()

        addExistingValue()

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
                    if(!image.isEmpty()){
                        editor.putString(Util.USER_PIC, image)
                        editor.apply()
                        Glide.with(requireContext()).load(image)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .into(binding.chatImageID)
                    }
                    val user = Customer(id, name, email, pass, status, image, address, phone)

                    binding.accountNameID.setText(name)
                    binding.accountEmailID.setText(email)
                    binding.accountAddressID.setText(address)
                    binding.accountPhnID.setText(phone)

                   /// Toast.makeText(requireContext(), sharedPreferences.getString(Util.USER_PIC,""), Toast.LENGTH_SHORT).show()

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

        binding = FragmentAccountBinding.inflate(inflater, container, false)
        userDB = Firebase.database.reference.child("user")
        progressDialog = ProgressDialog(requireContext())

        defDatabase = Firebase.database.reference
        binding.profilePicID.setOnClickListener {
            createPhoto()
        }
        binding.saveButtonID.setOnClickListener {
            createProfile()
        }

        return binding.root
    }

    private fun createPhoto() {
        CropImage.activity()
            .start(requireContext(), this);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri: Uri = result.uri
                storeImageIntoDB(resultUri)
                //Toast.makeText(requireContext(),imageUri.toString(),Toast.LENGTH_SHORT).show()

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun storeImageIntoDB(imageUri: Uri) {

        progressDialog.setTitle("Image Setting")
        progressDialog.setMessage("Please Wait...")
        progressDialog.show()
        val imageRef = storageRef.child("${System.currentTimeMillis()}profile.jpg")
        var uploadTask = imageRef.putFile(imageUri).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadedUrl = task.result.toString()
                //Toast.makeText(requireContext(), downloadedUrl, Toast.LENGTH_SHORT).show()

                Log.e("image", downloadedUrl)
                editor.putString(Util.USER_PIC, downloadedUrl)
                editor.apply()

                Glide.with(requireContext()).load(downloadedUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(binding.chatImageID)

                progressDialog.dismiss()

            } else {
                progressDialog.dismiss()
                task.exception?.let {
                    throw it
                }
            }

        }
    }

    private fun createProfile() {
        if (binding.accountNameID.text.toString().isEmpty()) {
            binding.accountNameID.setError("Name is empty!")
            binding.accountNameID.requestFocus()

        } else if (binding.accountEmailID.text.toString().isEmpty()) {
            binding.accountEmailID.setError("Email is empty!")
            binding.accountEmailID.requestFocus()

        } else if (binding.accountPhnID.text.toString().isEmpty()) {
            binding.accountPhnID.setError("Phone no. is empty!")
            binding.accountPhnID.requestFocus()

        } else if (binding.accountAddressID.text.toString().isEmpty()) {
            binding.accountAddressID.setError("Address is empty!")
            binding.accountAddressID.requestFocus()

        } else {
            progressDialog.setTitle("Profile Creation")
            progressDialog.setMessage("Your profile is being created.Please Wait...")
            progressDialog.show()

            val email = binding.accountEmailID.text.toString()
            val name = binding.accountNameID.text.toString()
            val password = sharedPreferences.getString(Util.USER_PASS,"")
            val status = sharedPreferences.getString("state","")
            val pic = sharedPreferences.getString(Util.USER_PIC,"")
            val addresses = binding.accountAddressID.text.toString()
            val id = Firebase.auth.currentUser!!.uid
            val phone = binding.accountPhnID.text.toString()
            val newUser = Customer(id,name,email,password!!,status!!,pic!!,addresses,phone)

            updateDb(newUser.id,newUser)

        }


    }

    private fun updateDb(id: String,user:Customer) {

        userDB.child(id).setValue(user).addOnCompleteListener { task->
            if(task.isSuccessful){

                if(sharedPreferences.getString("state","")=="customer"){
                    findSellerAndupdateChat(user)
                }
                else{
                    updateSellerChat(user)
                }

                updateChatView(user)
                progressDialog.dismiss()

                editor.putString(Util.USER_ADDRESS,user.address)
                editor.putString(Util.USER_PHONE,user.phone)
                editor.apply()

                Toasty.success(requireContext(),"Profile is saved successfully...").show()


                    defDatabase.child("profile").child(currentUserID).updateChildren(
                        mapOf(
                            "exist" to "1"
                        )
                    )


            }
            else{
                progressDialog.dismiss()
                Toasty.error(requireContext(),"Profile creation failed...").show()

            }
        }
    }

    private fun updateSellerChat(user: Customer) {

        defDatabase.child("chat").child(currentUserID).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(userID in snapshot.children){
                    Log.e("user_id",userID.key.toString())
                    for(targetId in userID.children){
                        Log.e("target_id",targetId.key.toString())
                        if(targetId.child("from").value.toString() == currentUserID)
                        {
                            defDatabase.child("chat").child(currentUserID)
                                .child(userID.key.toString()).child(targetId.key.toString())
                                .child("image").setValue(user.image)
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun findSellerAndupdateChat(user: Customer) {
        defDatabase.child("seller").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(id in snapshot.children){
                    Log.e("id:",id.value.toString())
                    updateChat(id.value.toString(),user)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun updateChatView(user: Customer) {
        defDatabase.child("chatView").child("view").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(child in snapshot.children){
                    for(data in child.children){

                        val uid = data.child("userID").value.toString()
                        if(uid == currentUserID){
                            defDatabase.child("chatView").child("view").child(child.key.toString())
                                .child(data.key.toString()).child("image")
                                .setValue(user.image)
                        }



                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun updateChat(sellerID:String,user: Customer) {

        defDatabase.child("chat").child(sellerID).child(currentUserID)
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(data in snapshot.children){
                        val chat = data.getValue(ChatMessage::class.java)
                        val image =user.image
                        if(chat!!.from == currentUserID){

                            defDatabase.child("chat").child(sellerID).child(currentUserID)
                                .child(data.key.toString()).child("image")
                                .setValue(image)
                        }
                        Log.e("key",data.key.toString())
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }

}