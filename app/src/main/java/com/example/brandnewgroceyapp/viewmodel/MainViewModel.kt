package com.example.brandnewgroceyapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.brandnewgroceyapp.model.Groceries
import com.example.brandnewgroceyapp.model.Grocery
import com.example.brandnewgroceyapp.repository.GroceryRepository
import com.example.brandnewgroceyapp.util.NetworkState
import com.example.brandnewgroceyapp.util.Util
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.lang.Error

@ActivityRetainedScoped
class MainViewModel @ViewModelInject constructor(
    application: Application, var repository: GroceryRepository
) : AndroidViewModel(application) {
    var allGroceries: MutableLiveData<NetworkState<Groceries>> = MutableLiveData()

    fun getAllGroceries() {
        viewModelScope.launch {

                safeGroceriesCall()


        }
    }




    private suspend fun safeGroceriesCall() {
        allGroceries.value = NetworkState.Load()

        val response = repository.remote.getGroceries()


        if (Util.hasInternetConnection(getApplication())) {

            allGroceries.value = handleNetworkState(response)
        }
        else{
           allGroceries.value = NetworkState.Error(response.body(),"No Internet Connection !!!")
        }
    }

    private fun handleNetworkState(response: Response<Groceries>): NetworkState<Groceries>? {

        return when {
            response.code() == 402 -> return NetworkState.Error(response.body(), "402 code error")
            response.body()!!.groceries.isNullOrEmpty() -> NetworkState.Error(
                response.body(),
                "grocery is empty"
            )
            response.isSuccessful -> NetworkState.Success(response.body())
            else -> NetworkState.Error(response.body(), response.message().toString())
        }
    }

    fun cacheDataLocally(grocery:Grocery){
        viewModelScope.launch(Dispatchers.IO) {

            repository.local.insertGroceryData(grocery)


        }
    }




}