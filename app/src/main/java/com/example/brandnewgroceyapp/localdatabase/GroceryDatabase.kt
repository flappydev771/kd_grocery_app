package com.example.brandnewgroceyapp.localdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.brandnewgroceyapp.model.Grocery
@Database(entities = [Grocery::class], version = 2, exportSchema = false)
abstract class GroceryDatabase:RoomDatabase() {
 abstract fun dao():GroceryDao
}