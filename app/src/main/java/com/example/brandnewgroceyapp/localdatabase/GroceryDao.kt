package com.example.brandnewgroceyapp.localdatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.brandnewgroceyapp.model.Grocery
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(grocery: Grocery)

    @Query("SELECT * FROM grocery_table WHERE name LIKE :name AND category LIKE :category")
    fun searchDatabase(name:String,category: String):Flow<List<Grocery>>

}