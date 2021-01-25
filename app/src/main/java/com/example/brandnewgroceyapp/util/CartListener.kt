package com.example.brandnewgroceyapp.util

import com.example.brandnewgroceyapp.model.Grocery

interface CartListener {

    fun listen(grocery: Grocery)


}