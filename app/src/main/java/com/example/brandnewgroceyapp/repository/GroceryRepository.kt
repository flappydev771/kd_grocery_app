package com.example.brandnewgroceyapp.repository

import com.example.brandnewgroceyapp.data.LocalData
import com.example.brandnewgroceyapp.data.RemoteData
import javax.inject.Inject

class GroceryRepository @Inject constructor(remoteData: RemoteData,localData: LocalData) {

    val remote = remoteData
    val local = localData


}