package com.example.brandnewgroceyapp.util

sealed class NetworkState<T>(
    val data:T?=null,
    val message:String?=null
) {

    class Success<T>(data: T?):NetworkState<T>(data)
    class Error<T>(data: T?,message: String?):NetworkState<T>(data,message)
    class Load<T>():NetworkState<T>()


}