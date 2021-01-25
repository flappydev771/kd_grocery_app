package com.example.brandnewgroceyapp.viewmodel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.brandnewgroceyapp.model.Groceries
import com.example.brandnewgroceyapp.model.Grocery
import com.example.brandnewgroceyapp.network.GroceryApi
import com.example.brandnewgroceyapp.repository.GroceryRepository
import com.example.brandnewgroceyapp.util.NetworkState
import com.example.brandnewgroceyapp.util.Util
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

@ActivityRetainedScoped
class GroceryByCategoryViewModel @ViewModelInject constructor(
    application: Application,
    private val repository: GroceryRepository,
    private val api: GroceryApi
) : AndroidViewModel(application) {


    val groceryByCategory: MutableLiveData<NetworkState<Groceries>> = MutableLiveData()

    ///category
    fun getGroceryByCategory(category: String) {
        viewModelScope.launch {

                safeCallToGroceryByCategory(category)


        }
    }

    suspend fun safeCallToGroceryByCategory(category: String) {
        groceryByCategory.value = NetworkState.Load()
        val response = repository.remote.getGroceryByCategory(category)

        if (Util.hasInternetConnection(getApplication())) {
            groceryByCategory.value = handleNetWorkState(response)
        } else {
            groceryByCategory.value =
                NetworkState.Error(response.body(), "No Internet Connection...")
        }
    }


    private fun handleNetWorkState(response: Response<Groceries>): NetworkState<Groceries>? {

        return when {
            response.code() == 402 -> NetworkState.Error(response.body(), "402 code error")
            response.body()!!.groceries.isNullOrEmpty() -> NetworkState.Error(
                response.body(),
                "data is empty"
            )
            response.isSuccessful -> NetworkState.Success(response.body())
            else -> NetworkState.Error(response.body(), response.message().toString())
        }

    }



   fun groceryByName(name:String,category: String):LiveData<List<Grocery>> = repository.local.searchDatabase(name,category).asLiveData()






}