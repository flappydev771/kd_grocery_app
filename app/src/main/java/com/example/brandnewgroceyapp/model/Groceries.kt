package com.example.brandnewgroceyapp.model

import com.google.gson.annotations.SerializedName

data class Groceries (
    @SerializedName("details")
    var groceries:List<Grocery>
)