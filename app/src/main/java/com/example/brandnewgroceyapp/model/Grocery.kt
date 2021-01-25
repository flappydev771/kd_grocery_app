package com.example.brandnewgroceyapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "grocery_table")

data class Grocery(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    var name: String,

    var category: String,

    var price1: String,

    var price2: String,

    var stock: String,

    var description: String,

    var image: String


)
