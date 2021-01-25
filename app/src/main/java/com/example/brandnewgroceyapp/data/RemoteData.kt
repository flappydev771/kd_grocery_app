package com.example.brandnewgroceyapp.data

import androidx.lifecycle.LiveData
import com.example.brandnewgroceyapp.model.Customer
import com.example.brandnewgroceyapp.model.Groceries
import com.example.brandnewgroceyapp.network.GroceryApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import retrofit2.Response
import java.util.concurrent.Flow
import javax.inject.Inject

class RemoteData @Inject constructor(private val api: GroceryApi) {

    suspend fun getGroceries():Response<Groceries>{
        return api.getAllGroceries()
    }
    suspend fun getGroceryByCategory(category: String):Response<Groceries>{
       return api.getGroceryByCategory(category)
    }
    suspend fun getGroceryByName(name: String):Response<Groceries>{
        return api.getGroceryByCategory(name)
    }

}